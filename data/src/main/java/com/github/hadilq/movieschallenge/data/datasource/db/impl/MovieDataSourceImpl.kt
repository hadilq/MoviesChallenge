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
package com.github.hadilq.movieschallenge.data.datasource.db.impl

import androidx.paging.DataSource
import com.github.hadilq.movieschallenge.data.datasource.api.PopularMovieDataSource
import com.github.hadilq.movieschallenge.data.datasource.db.MovieDataSource
import com.github.hadilq.movieschallenge.data.datasource.map
import com.github.hadilq.movieschallenge.data.db.dao.MovieDao
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity

class MovieDataSourceImpl(
    private val dao: MovieDao
) : MovieDataSource {
    override fun popular(): DataSource.Factory<Int, MovieEntity> = dao.loadAll().map { it.map() }

    override fun all(): List<MovieEntity> = dao.all().map { it.map() }

    override fun save(list: PopularMovieDataSource.MoviesList) = dao.save(*list.map().toTypedArray())

    override fun deleteAll() = dao.deleteAll()
}