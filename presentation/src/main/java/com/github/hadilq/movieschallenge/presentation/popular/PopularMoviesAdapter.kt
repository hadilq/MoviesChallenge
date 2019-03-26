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
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import javax.inject.Inject
import javax.inject.Provider

@MoviesScope
class PopularMoviesAdapter @Inject constructor(
    private val provider: Provider<MovieViewHolder>,
    private val bridge: MoviesViewHolderBridge
) : PagedListAdapter<MovieEntity, MovieViewHolder>(MOVIES_DIFF) {

    lateinit var listener: (MovieEntity) -> Unit

    var endOfList: Boolean = false
        set(value) {
            val wasEndOfList = endOfList
            field = value
            if (value && !wasEndOfList) {
                notifyItemRemoved(itemCount)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        bridge.parent = parent
        val viewHolder = provider.get()
        viewHolder.listener = listener
        return viewHolder
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemCount(): Int {
        val itemCount = super.getItemCount()
        return if (itemCount == 0) 0 else itemCount + if (endOfList) 0 else 1
    }

    override fun getItem(position: Int): MovieEntity? {
        val itemCount = itemCount
        if (itemCount != 0 && position == itemCount - 1 && !endOfList) {
            return null
        }
        return super.getItem(position)
    }

    companion object {
        val MOVIES_DIFF = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem == newItem
        }
    }
}