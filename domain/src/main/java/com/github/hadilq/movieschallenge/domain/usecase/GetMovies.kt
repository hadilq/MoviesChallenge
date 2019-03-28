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
package com.github.hadilq.movieschallenge.domain.usecase

import androidx.paging.PagedList
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import com.github.hadilq.movieschallenge.domain.entity.ResultState
import io.reactivex.Flowable

interface GetMovies {

    /**
     * Returns a stream of ResultStates to load movies.
     */
    fun loadMovies(refresh: Boolean): Flowable<ResultState<PagedList<MovieEntity>>>

    /**
     * Retries the last failed request to server.
     */
    fun retry()
}