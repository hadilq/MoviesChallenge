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

import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import com.github.hadilq.movieschallenge.presentation.R
import javax.inject.Inject
import javax.inject.Provider

@MoviesScope
class PopularMoviesAdapter @Inject constructor(
    private val movieProvider: Provider<MovieViewHolder>,
    private val loadingProvider: Provider<LoadingViewHolder>,
    private val bridge: MoviesViewHolderBridge
) : PagedListAdapter<MovieEntity, RecyclerView.ViewHolder>(MOVIES_DIFF) {

    lateinit var listener: (MovieEntity) -> Unit

    private var listSize = 0
    var loading: Boolean = false
        set(value) {
            val previousState = loading
            val hadLoadingRow = hasLoadingRow()
            field = value
            val hasLoadingRow = hasLoadingRow()
            if (hadLoadingRow != hasLoadingRow) {
                if (hadLoadingRow) {
//                    notifyItemRemoved(super.getItemCount())
                    notifyDataSetChanged()
                } else {
                    notifyItemInserted(super.getItemCount())
                }
            } else if (hasLoadingRow && previousState != value) {
                notifyItemChanged(itemCount - 1)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        bridge.parent = parent
        return when (viewType) {
            R.layout.movie -> {
                val vh = movieProvider.get()
                vh.listener = listener
                vh
            }
            R.layout.loading -> loadingProvider.get()
            else -> throw IllegalStateException("Unknown type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.movie -> (holder as MovieViewHolder).onBind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (position == itemCount - 1 && hasLoadingRow()) R.layout.loading else R.layout.movie

    override fun getItemCount(): Int = super.getItemCount() + if (hasLoadingRow()) 1 else 0

    override fun submitList(pagedList: PagedList<MovieEntity>) {
        listSize = pagedList.size
        super.submitList(pagedList)
    }

    fun listSize() = listSize

    private fun hasLoadingRow() = if (super.getItemCount() == 0) false else loading

    companion object {
        val MOVIES_DIFF = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem == newItem
        }
    }
}