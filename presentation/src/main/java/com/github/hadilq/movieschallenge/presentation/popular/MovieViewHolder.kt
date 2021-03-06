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

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import com.github.hadilq.movieschallenge.presentation.R
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.squareup.picasso.Picasso
import inflate
import kotlinx.android.synthetic.main.movie.view.*
import loadFromUrl


class MovieViewHolder(
    view: View,
    private val picasso: Picasso
) : RecyclerView.ViewHolder(view) {

    lateinit var listener: (MovieEntity) -> Unit
    private var item: MovieEntity? = null

    @AssistedInject
    constructor(@Assisted parent: ViewGroup, picasso: Picasso) : this(
        parent.inflate(R.layout.movie),
        picasso
    )

    init {
        itemView.setOnClickListener { item?.let { listener(it) } }
    }

    @SuppressLint("SetTextI18n")
    fun onBind(item: MovieEntity?) {
        this.item = item
        item?.apply {
            backdropPath?.apply {
                itemView.imageView.loadFromUrl(picasso, this)
            } ?: also {
                picasso.cancelRequest(itemView.imageView)
                itemView.imageView.setImageDrawable(null)
                Log.d("MovieViewHolder", "It shouldn't happen, so log it to the crash service. ${item.name}")
            }
            itemView.titleView.text = name
            itemView.averageRatingView.text = " $voteAverage"
        } ?: let {
            throw IllegalStateException("Item cannot be null")
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(parent: ViewGroup): MovieViewHolder
    }
}
