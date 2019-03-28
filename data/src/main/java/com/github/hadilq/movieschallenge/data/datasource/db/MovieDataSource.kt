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
package com.github.hadilq.movieschallenge.data.datasource.db

import androidx.paging.DataSource
import com.github.hadilq.movieschallenge.data.datasource.api.PopularMovieDataSource
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity

interface MovieDataSource {

    /**
     * Returns the popular movies.
     */
    fun popular(): DataSource.Factory<Int, MovieEntity>

    /**
     * Returns every available movies.
     */
    fun all(): List<MovieEntity>

    /**
     * Saves the list of popular movies.
     */
    fun save(list: PopularMovieDataSource.MoviesList)

    /**
     * Deletes all the movies in the table.
     */
    fun deleteAll()
}