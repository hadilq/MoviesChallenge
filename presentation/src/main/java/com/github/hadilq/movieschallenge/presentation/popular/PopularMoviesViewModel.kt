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
package com.github.hadilq.movieschallenge.presentation.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.github.hadilq.movieschallenge.domain.entity.Error
import com.github.hadilq.movieschallenge.domain.entity.Loading
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import com.github.hadilq.movieschallenge.domain.entity.Success
import com.github.hadilq.movieschallenge.domain.usecase.GetMovies
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PopularMoviesViewModel @Inject constructor(
    private val getMovies: GetMovies
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private var started = false

    private val _successLiveData by lazy {
        MutableLiveData<PagedList<MovieEntity>>()
    }
    private val _errorLiveData by lazy {
        MutableLiveData<Throwable>()
    }
    private val _loadingLiveData by lazy {
        MutableLiveData<Boolean>()
    }
    val successLiveData: LiveData<PagedList<MovieEntity>> = _successLiveData
    val errorLiveData: LiveData<Throwable> = _errorLiveData
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun retry() {
        started = false
        startLoading()
    }

    fun startLoading() {
        if (started) {
            return
        }
        started = true
        disposables.clear()
        getMovies.loadMovies().subscribe { result ->
            when (result) {
                is Success -> _successLiveData.postValue(result.data)
                is Error -> _errorLiveData.postValue(result.throwable)
                is Loading -> _loadingLiveData.postValue(result.loading)
            }
        }.track()
    }

    private fun Disposable.track() {
        disposables.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}