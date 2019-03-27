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
package com.github.hadilq.movieschallenge.data.datasource.api.impl

import com.github.hadilq.movieschallenge.data.api.Api
import com.github.hadilq.movieschallenge.data.api.dto.PopularDto
import com.github.hadilq.movieschallenge.data.datasource.api.PopularMovieDataSource
import com.github.hadilq.movieschallenge.data.datasource.map
import io.reactivex.Single

class PopularMovieDataSourceImpl(
    private val api: Api
) : PopularMovieDataSource {

    override fun call(page: Int): Single<PopularMovieDataSource.MoviesList> =
        api.getPopular(page = page).map(PopularDto::map)
}