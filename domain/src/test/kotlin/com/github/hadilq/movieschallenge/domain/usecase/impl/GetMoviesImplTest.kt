package com.github.hadilq.movieschallenge.domain.usecase.impl

import com.github.hadilq.movieschallenge.domain.repository.ApiKeyRepository
import com.github.hadilq.movieschallenge.domain.repository.MovieRepository
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class GetMoviesImplTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var apiKeyRepository: ApiKeyRepository

    @Before
    fun setup() {
        movieRepository = mock()
        apiKeyRepository = mock()
    }

    @Test
    fun loadMovies() {
        // Given
        `when`(apiKeyRepository.apiKey()).doReturn("apiKey")
        val usecase = GetMoviesImpl(movieRepository, apiKeyRepository)

        // When
        usecase.loadMovies()

        // Then
        verify(movieRepository).loadMovies("apiKey")
    }
}