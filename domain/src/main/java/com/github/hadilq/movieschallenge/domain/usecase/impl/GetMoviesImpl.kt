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
package com.github.hadilq.movieschallenge.domain.usecase.impl

import androidx.paging.PagedList
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import com.github.hadilq.movieschallenge.domain.entity.ResultState
import com.github.hadilq.movieschallenge.domain.repository.ApiKeyRepository
import com.github.hadilq.movieschallenge.domain.repository.MovieRepository
import com.github.hadilq.movieschallenge.domain.usecase.GetMovies
import io.reactivex.Flowable

class GetMoviesImpl(
    private val movieRepository: MovieRepository,
    private val apiKeyRepository: ApiKeyRepository
) : GetMovies {

    override fun loadMovies(): Flowable<ResultState<PagedList<MovieEntity>>> {
        ArrayList<String>().toTypedArray().map {  }
        return movieRepository.loadMovies(apiKeyRepository.apiKey())
    }
}