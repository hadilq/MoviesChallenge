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
package com.github.hadilq.movieschallenge.presentation.popular

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import com.github.hadilq.movieschallenge.presentation.BuildConfig
import com.github.hadilq.movieschallenge.presentation.R
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import gone
import kotlinx.android.synthetic.main.activity_main.*
import visible
import javax.inject.Inject

class PopularMoviesActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var adapter: PopularMoviesAdapter

    private lateinit var viewModel: PopularMoviesViewModel
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = viewModel(viewModelFactory) {
            successLiveData.observe(::success)
            errorLiveData.observe(::error)
            loadingLiveData.observe(::loading)
        }

        initView()
        viewModel.startLoading()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.listener = {
            // item clicked
        }
        swipeView.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun success(list: PagedList<MovieEntity>) {
        adapter.listSize = list.size
        adapter.submitList(list)
    }

    private fun error(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e("PopularMoviesActivity", "An error happened!", throwable)
        }
        showFailure(R.string.errorMessage) { viewModel.retry() }
    }

    private fun loading(loading: Boolean) {
        adapter.loading = loading
        swipeView.isRefreshing = false
        if (loading && adapter.listSize == 0) {
            progressView.visible()
        } else {
            progressView.gone()
        }
    }

    private fun showFailure(errorMessage: Int, retry: (View) -> Unit) {
        if (snackbar?.isShown == true) {
            snackbar!!.dismiss()
        }
        snackbar = Snackbar.make(coordinator, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) {
                snackbar = null
                retry(it)
            }
        snackbar!!.show()
    }

    private inline fun <reified T : ViewModel> viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
        val vm = ViewModelProviders.of(this, factory)[T::class.java]
        vm.body()
        return vm
    }

    private inline fun <T> LiveData<T>.observe(crossinline block: (T) -> Unit) {
        observe(this@PopularMoviesActivity, Observer { it?.let { t -> block(t) } })
    }
}
