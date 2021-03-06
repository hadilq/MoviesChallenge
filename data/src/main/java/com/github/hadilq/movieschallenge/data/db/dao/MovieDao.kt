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
package com.github.hadilq.movieschallenge.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.hadilq.movieschallenge.data.db.table.MovieTable

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY sorting")
    fun loadAll(): DataSource.Factory<Int, MovieTable>

    @Query("SELECT * FROM movie")
    fun all(): List<MovieTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg movie: MovieTable)

    @Query("DELETE FROM movie")
    fun deleteAll()
}