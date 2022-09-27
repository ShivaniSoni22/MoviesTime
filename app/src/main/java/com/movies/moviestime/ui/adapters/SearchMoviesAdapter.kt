package com.movies.moviestime.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.moviestime.database.entity.model.Search
import com.movies.moviestime.databinding.LayoutAllMoviesBinding
import com.movies.moviestime.ui.adapters.callbacks.MoviesDiffCallback

class SearchMoviesAdapter :
    ListAdapter<Search, SearchMoviesAdapter.SearchMoviesViewHolder>(MoviesDiffCallback()) {

    class SearchMoviesViewHolder private constructor(private val binding: LayoutAllMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Search) {
            binding.movie = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SearchMoviesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutAllMoviesBinding.inflate(layoutInflater, parent, false)
                return SearchMoviesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMoviesViewHolder {
        return SearchMoviesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchMoviesViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

}