package com.github.hadilq.movieschallenge.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.github.hadilq.movieschallenge.data.datasource.IMAGE_PREFIX_ORIGIN
import com.github.hadilq.movieschallenge.data.datasource.IMAGE_PREFIX_W500
import com.github.hadilq.movieschallenge.data.datasource.api.PopularMovieDataSource
import com.github.hadilq.movieschallenge.data.datasource.db.impl.MovieDataSourceImpl
import com.github.hadilq.movieschallenge.data.db.AppDatabase
import com.github.hadilq.movieschallenge.domain.entity.Loading
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import com.github.hadilq.movieschallenge.domain.entity.Success
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class MovieRepositoryImplTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var movies: MovieDataSourceImpl
    private lateinit var popularMovies: PopularMovieDataSource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        movies = MovieDataSourceImpl(database.movieDao())
        popularMovies = mock()

        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun close() {
        RxJavaPlugins.reset()
        database.close()
    }

    @Test
    fun loadMovies() {
        val results = ArrayList<MovieEntity>().apply {
            add(MovieEntity(0, "one", 10f, 100, 0.5f, "$IMAGE_PREFIX_W500/slfn", "$IMAGE_PREFIX_ORIGIN/kdjfnv"))
            add(MovieEntity(1, "two", 8f, 50, 0.5f, null, "$IMAGE_PREFIX_ORIGIN/nvvv"))
        }
        `when`(popularMovies.call(any())).doReturn(
            Single.just(
                PopularMovieDataSource.MoviesList(
                    1,
                    2,
                    1,
                    results
                )
            )
        )
        val repository = MovieRepositoryImpl(popularMovies, movies)

        val flowable = repository.loadMovies(false).test()

        flowable.assertNotComplete()
        flowable.assertNoErrors()
        assertEquals(2, flowable.valueCount())
        assertEquals(results, ArrayList((flowable.values()[0] as Success<PagedList<MovieEntity>>).data))
        assertEquals(
            ArrayList<MovieEntity>(),
            ArrayList((flowable.values()[1] as Success<PagedList<MovieEntity>>).data)
        )
    }
}