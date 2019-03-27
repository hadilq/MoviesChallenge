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
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.movieschallenge.domain.entity.MovieEntity
import com.github.hadilq.movieschallenge.presentation.R
import com.squareup.picasso.Picasso
import gone
import inflate
import kotlinx.android.synthetic.main.movie.view.*
import loadFromUrl
import visible
import javax.inject.Inject

class MovieViewHolder(
    view: View,
    private val picasso: Picasso
) : RecyclerView.ViewHolder(view) {

    lateinit var listener: (MovieEntity) -> Unit
    private var item: MovieEntity? = null

    @Inject
    constructor(bridge: MoviesViewHolderBridge, picasso: Picasso) : this(
        bridge.parent.inflate(R.layout.movie),
        picasso
    )

    init {
        itemView.setOnClickListener { item?.let { listener(it) } }
    }

    @SuppressLint("SetTextI18n")
    fun onBind(item: MovieEntity?) {
        this.item = item
        item?.apply {
            itemView.progressView.gone()

            backdropPath?.apply {
                itemView.imageView.loadFromUrl(picasso, this)
            } ?: also {
                // It shouldn't happen, so log it to the crash service
            }
            itemView.titleView.text = name
            itemView.averageRatingView.text = " $voteAverage"
        } ?: run {
            itemView.progressView.visible()
            itemView.titleView.gone()
            itemView.averageRatingView.gone()
            itemView.imageView.gone()
        }
    }
}
