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
package com.github.hadilq.movieschallenge.data.datasource

import com.github.hadilq.movieschallenge.data.api.dto.MovieDto
import com.github.hadilq.movieschallenge.data.api.dto.PopularDto
import com.github.hadilq.movieschallenge.data.datasource.api.PopularMovieDataSource
import com.github.hadilq.movieschallenge.data.db.table.MovieTable
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity

fun PopularDto.map() = PopularMovieDataSource.MoviesList(
    page = page,
    totalPages = totalPages,
    totalResults = totalResults,
    results = results.map { it.map() }
)

fun MovieDto.map() = MovieEntity(
    id = id,
    name = name,
    popularity = popularity,
    voteCount = voteCount,
    voteAverage = voteAverage,
    backdropPath = backdropPath,
    posterPath = posterPath
)

fun MovieTable.map() = MovieEntity(
    id = id,
    name = name,
    popularity = popularity,
    voteCount = voteCount,
    voteAverage = voteAverage,
    backdropPath = backdropPath,
    posterPath = posterPath
)

fun PopularMovieDataSource.MoviesList.map(): List<MovieTable> = ArrayList<MovieTable>().also { list ->
    results.forEach { movie -> list.add(movie.map()) }
}

fun MovieEntity.map() = MovieTable(
    id = id,
    name = name,
    popularity = popularity,
    voteCount = voteCount,
    voteAverage = voteAverage,
    backdropPath = backdropPath,
    posterPath = posterPath
)