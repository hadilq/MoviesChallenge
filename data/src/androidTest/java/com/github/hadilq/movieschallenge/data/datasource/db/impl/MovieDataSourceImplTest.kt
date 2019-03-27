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

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.github.hadilq.movieschallenge.data.datasource.*
import com.github.hadilq.movieschallenge.data.datasource.api.PopularMovieDataSource
import com.github.hadilq.movieschallenge.data.db.AppDatabase
import com.github.hadilq.movieschallenge.data.db.table.MovieTable
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDataSourceImplTest {
    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun save() {
        val movies = arrayOf(
            MovieTable(
                0, "one", 43f, 3, null, " ", 9.0f
            ), MovieTable(
                1, "one", 3f, 8, "", " ", 2f
            )
        )

        database.movieDao().save(*movies)

        assertEquals(movies, database.movieDao().all().toTypedArray())
    }

    @Test
    fun saveByDataSource() {
        val movies = arrayListOf(
            MovieEntity(
                0, "one", 43f, 3, 9.0f, null, "$IMAGE_PREFIX_ORIGIN/lsjfn"
            ), MovieEntity(
                1, "one", 3f, 8, 4f, "$IMAGE_PREFIX_W500/sfdv", "$IMAGE_PREFIX_ORIGIN/lsjfn"
            )
        )

        val dataSource = MovieDataSourceImpl(database.movieDao())

        dataSource.save(PopularMovieDataSource.MoviesList(1, 100, 200, movies))

        assertEquals(movies, dataSource.all())
    }

    @Test
    fun mapToBackdropUrl() {
        val s = "/sfvn"
        assertEquals(s, s.mapToBackdropUrl().mapFromBackdropUrl())
    }

    @Test
    fun mapToPosterUrl() {
        val s = "/sfvn"
        assertEquals(s, s.mapToPosterUrl().mapFromPosterUrl())
    }
}