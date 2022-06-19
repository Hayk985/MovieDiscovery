package com.moviediscovery.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviediscovery.app.loadImage
import com.moviediscovery.databinding.ItemMovieBinding
import com.moviediscovery.model.Movie
import com.moviediscovery.utils.UrlHelper

class MovieAdapter(
    private val onMovieClicked: (movieId: Int) -> Unit,
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var diffUtilCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)
    var movies: List<Movie>
        get() = differ.currentList.toMutableList()
        set(value) {
            differ.submitList(value)
        }

    fun addOnDataUpdatedListener(onDataUpdated: () -> Unit) {
        /**
         * This check is required to make sure we need to scroll recyclerview to the top
         */
        differ.addListListener { prevList, currentList ->
            if (prevList.size >= currentList.size)
                onDataUpdated()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                imgMovie.loadImage(UrlHelper.getPosterUrl(movie.posterPath))
                tvMovieTitle.text = movie.title
                tvMovieDescription.text = movie.overview
                root.setOnClickListener {
                    onMovieClicked(movie.id)
                }
            }
        }
    }
}