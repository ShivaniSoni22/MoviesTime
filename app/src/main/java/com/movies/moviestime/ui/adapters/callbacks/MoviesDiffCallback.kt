package com.movies.moviestime.ui.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.movies.moviestime.database.entity.model.Search

class MoviesDiffCallback : DiffUtil.ItemCallback<Search>() {

    override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
        return oldItem == newItem
    }

}