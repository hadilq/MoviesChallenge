package com.github.hadilq.movieschallenge.presentation.popular

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.github.hadilq.movieschallenge.domain.entity.*
import com.github.hadilq.movieschallenge.domain.usecase.GetMovies
import com.nhaarman.mockito_kotlin.*
import io.reactivex.processors.PublishProcessor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`

class PopularMoviesViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var getMovies: GetMovies
    private lateinit var processor: PublishProcessor<ResultState<PagedList<MovieEntity>>>
    private lateinit var viewModel: PopularMoviesViewModel

    @Before
    fun setup() {
        getMovies = mock()
        processor = PublishProcessor.create()
        `when`(getMovies.loadMovies(any())).doReturn(processor)

        viewModel = PopularMoviesViewModel(getMovies)
    }

    @Test
    fun startLoadingSuccess() {
        val observer = mock<Observer<PagedList<MovieEntity>>>()
        val pagedList = mock<PagedList<MovieEntity>>()
        viewModel.successLiveData.observeForever(observer)
        viewModel.startLoading()

        processor.onNext(Success(pagedList))

        verify(observer).onChanged(pagedList)
    }

    @Test
    fun startLoadingError() {
        val observer = mock<Observer<Throwable>>()
        val throwable = RuntimeException("")

        viewModel.errorLiveData.observeForever(observer)
        viewModel.startLoading()

        processor.onNext(Error(throwable))

        verify(observer).onChanged(throwable)
    }

    @Test
    fun startLoadingLoadingTrue() {
        val observer = mock<Observer<Boolean>>()

        viewModel.loadingLiveData.observeForever(observer)
        viewModel.startLoading()

        processor.onNext(Loading(true))

        verify(observer).onChanged(true)
    }

    @Test
    fun startLoadingLoadingFalse() {
        val observer = mock<Observer<Boolean>>()

        viewModel.loadingLiveData.observeForever(observer)
        viewModel.startLoading()

        processor.onNext(Loading(false))

        verify(observer).onChanged(false)
    }

    @Test
    fun refreshSuccess() {
        val observer = mock<Observer<PagedList<MovieEntity>>>()
        val pagedList = mock<PagedList<MovieEntity>>()
        viewModel.successLiveData.observeForever(observer)
        viewModel.refresh()

        processor.onNext(Success(pagedList))

        verify(observer).onChanged(pagedList)
    }

    @Test
    fun refreshAfterStartLoading() {
        val observer = mock<Observer<PagedList<MovieEntity>>>()
        val pagedList = mock<PagedList<MovieEntity>>()
        viewModel.successLiveData.observeForever(observer)
        viewModel.startLoading()

        processor.onNext(Success(pagedList))

        verify(observer).onChanged(pagedList)

        viewModel.refresh()

        processor.onNext(Success(pagedList))

        verify(observer, times(2)).onChanged(pagedList)

    }

}