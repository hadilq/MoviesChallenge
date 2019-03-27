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
package com.github.hadilq.movieschallenge.di

import com.github.hadilq.movieschallenge.data.api.Api
import com.github.hadilq.movieschallenge.data.datasource.api.PopularMovieDataSource
import com.github.hadilq.movieschallenge.data.datasource.api.impl.PopularMovieDataSourceImpl
import com.github.hadilq.movieschallenge.data.datasource.db.MovieDataSource
import com.github.hadilq.movieschallenge.data.datasource.db.impl.MovieDataSourceImpl
import com.github.hadilq.movieschallenge.data.db.dao.MovieDao
import com.github.hadilq.movieschallenge.data.repository.MovieRepositoryImpl
import com.github.hadilq.movieschallenge.domain.repository.ApiKeyRepository
import com.github.hadilq.movieschallenge.domain.repository.MovieRepository
import com.github.hadilq.movieschallenge.domain.usecase.GetMovies
import com.github.hadilq.movieschallenge.domain.usecase.impl.GetMoviesImpl
import dagger.Module
import dagger.Provides

@Module
class PopularMoviesModule {

    @Provides
    fun provideDatabaseSource(dao: MovieDao): MovieDataSource = MovieDataSourceImpl(dao)

    @Provides
    fun provideApiSource(api: Api): PopularMovieDataSource = PopularMovieDataSourceImpl(api)

    @Provides
    fun provideRepository(
        apiSource: PopularMovieDataSource,
        databaseSource: MovieDataSource
    ): MovieRepository = MovieRepositoryImpl(apiSource, databaseSource)

    @Provides
    fun provideUseCase(
        repository: MovieRepository,
        apiKeyRepository: ApiKeyRepository
    ): GetMovies = GetMoviesImpl(repository, apiKeyRepository)
}
