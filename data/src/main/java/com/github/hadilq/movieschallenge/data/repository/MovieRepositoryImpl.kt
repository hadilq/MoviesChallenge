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

class MovieRepositoryImpl(
    private val popularMovies: PopularMovieDataSource,
    private val movies: MovieDataSource
) : MovieRepository {

    override fun loadMovies(apiKey: String): Flowable<ResultState<PagedList<MovieEntity>>> {
        val processor = PublishProcessor.create<ResultState<PagedList<MovieEntity>>>()
        val disposables = CompositeDisposable()

        processor.onNext(Loading(true))

        return processor.flatMap {
            RxPagedListBuilder(
                movies.popular(),
                config
            )
                .setBoundaryCallback(BoundaryCallback(apiKey, processor, disposables))
                .buildFlowable(BackpressureStrategy.BUFFER)
                .map { Success(it) as ResultState<PagedList<MovieEntity>> }
        }.hide().doOnCancel { disposables.clear() }
    }

    inner class BoundaryCallback(
        private val apiKey: String,
        private val processor: PublishProcessor<ResultState<PagedList<MovieEntity>>>,
        private val disposables: CompositeDisposable
    ) : PagedList.BoundaryCallback<MovieEntity>() {

        private var page = 1
        private var endOfList = false

        override fun onZeroItemsLoaded() {
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
            popularMovies.call(apiKey, page)
                .subscribe({
                    if (it.totalPages <= page) {
                        endOfList = true
                        processor.onNext(Loading(false))
                    }
                    movies.save(it)
                }) {
                    processor.onNext(Error(it))
                }
                .track()
        }

        private fun Disposable.track() {
            disposables.add(this)
        }
    }

    companion object {
        private val config by lazy {
            PagedList.Config.Builder()
                .setPageSize(20)
                .setInitialLoadSizeHint(20)
                .setPrefetchDistance(5)
                .setEnablePlaceholders(false)
                .build()
        }
    }
}