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
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

class PopularMovieDataSourceImplTest {

    private lateinit var api: Api
    private val gson = Gson()

    @Before
    fun setup() {
        api = mock()
    }

    @Test
    fun call() {
        val apiKey = "apiKey"
        val json = readString("popular-movies.json")
        val dto = gson.fromJson(json, PopularDto::class.java)
        `when`(api.getPopular(apiKey, 1)).doReturn(Single.just(dto))

        val test = api.getPopular(apiKey, 1).test()

        test.assertComplete()
        test.assertNoErrors()
        test.assertValueCount(1)

        assertEquals(20000, test.values()[0].totalResults)
        assertEquals(1000, test.values()[0].totalPages)
        assertEquals(20, test.values()[0].results.size)
    }


    @Test
    fun callFromDataSource() {
        val apiKey = "apiKey"
        `when`(api.getPopular(apiKey, 1)).doReturn(
            Single.just(
                gson.fromJson(
                    readString("popular-movies.json"),
                    PopularDto::class.java
                )
            )
        )
        val dataSource = PopularMovieDataSourceImpl(api)

        val test = dataSource.call(apiKey, 1).test()

        test.assertComplete()
        test.assertNoErrors()
        test.assertValueCount(1)

        assertEquals(20000, test.values()[0].totalResults)
        assertEquals(1000, test.values()[0].totalPages)
        assertEquals(20, test.values()[0].results.size)
    }

    @Throws(Exception::class)
    fun readString(file: String): String {
        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(file)
        val writer = StringWriter()

        val buffer = CharArray(1024)
        inputStream.use { stream ->
            val reader = BufferedReader(
                InputStreamReader(stream, "UTF-8")
            )
            var n: Int
            while (run { n = reader.read(buffer); n } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        return writer.toString()

    }
}