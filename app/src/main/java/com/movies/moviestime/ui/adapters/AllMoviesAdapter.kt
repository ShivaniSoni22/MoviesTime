package com.movies.moviestime.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movies.moviestime.database.entity.model.Search
import com.movies.moviestime.databinding.LayoutAllMoviesBinding
import com.movies.moviestime.ui.adapters.callbacks.MoviesDiffCallback

class AllMoviesAdapter :
    ListAdapter<Search, AllMoviesAdapter.AllMoviesViewHolder>(MoviesDiffCallback()) {

    class AllMoviesViewHolder private constructor(private val binding: LayoutAllMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Search) {
            binding.movie = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AllMoviesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutAllMoviesBinding.inflate(layoutInflater, parent, false)
                return AllMoviesViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMoviesViewHolder {
        return AllMoviesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AllMoviesViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

}