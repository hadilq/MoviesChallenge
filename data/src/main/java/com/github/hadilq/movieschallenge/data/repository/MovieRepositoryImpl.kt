/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
package com.github.hadilq.movieschallenge.data.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.github.hadilq.movieschallenge.data.datasource.api.PopularMovieDataSource
import com.github.hadilq.movieschallenge.data.datasource.db.MovieDataSource
import com.github.hadilq.movieschallenge.domain.entity.*
import com.github.hadilq.movieschallenge.domain.repository.MovieRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

class MovieRepositoryImpl(
    private val popularMovies: PopularMovieDataSource,
    private val movies: MovieDataSource
) : MovieRepository {

    private lateinit var boundaryCallback: BoundaryCallback

    override fun loadMovies(refresh: Boolean): Flowable<ResultState<PagedList<MovieEntity>>> {
        val processor = PublishProcessor.create<ResultState<PagedList<MovieEntity>>>()
        val disposables = CompositeDisposable()

        return Flowable.merge(
            databaseFlowable(refresh, processor, disposables),
            processor.hide().doOnCancel {
                disposables.clear()
            }
        )
    }

    override fun retry() {
        boundaryCallback.retry()
    }

    private fun databaseFlowable(
        refresh: Boolean,
        processor: PublishProcessor<ResultState<PagedList<MovieEntity>>>,
        disposables: CompositeDisposable
    ): Flowable<ResultState<PagedList<MovieEntity>>> {
        val factory = movies.popular()

        boundaryCallback = BoundaryCallback(refresh, processor, disposables)
        return RxPagedListBuilder(factory, config)
            .setBoundaryCallback(boundaryCallback)
            .buildFlowable(BackpressureStrategy.BUFFER)
            .map { Success(it) as ResultState<PagedList<MovieEntity>> }
            .flatMap {
                Flowable.concat(Flowable.just(it, Loading(!boundaryCallback.endOfList())), processor)
            }
    }

    inner class BoundaryCallback(
        private val refresh: Boolean,
        private val processor: PublishProcessor<ResultState<PagedList<MovieEntity>>>,
        private val disposables: CompositeDisposable
    ) : PagedList.BoundaryCallback<MovieEntity>() {

        private var page = 0
        private var endOfList = false

        fun endOfList() = endOfList

        override fun onZeroItemsLoaded() {
            if (page == 1) {
                return
            }
            page = 1
            request(page)
        }

        override fun onItemAtEndLoaded(itemAtEnd: MovieEntity) {
            request(++page)
        }

        private fun request(page: Int) {
            if (endOfList) {
                return
            }
            processor.onNext(Loading(true))
            popularMovies.call(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.totalPages <= page) {
                        endOfList = true
                    }
                    if (refresh) {
                        movies.deleteAll()
                    }
                    movies.save(it)
                }) {
                    processor.onNext(Loading(false))
                    processor.onNext(Error(it))
                }
                .track()
        }

        private fun Disposable.track() {
            disposables.add(this)
        }

        fun retry() {
            request(page)
        }
    }

    companion object {
        private val config by lazy {
            PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setPrefetchDistance(5)
                .setEnablePlaceholders(false)
                .build()
        }

        const val PAGE_SIZE = 20
    }
}