package com.moviediscovery.app

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.moviediscovery.R
import com.moviediscovery.api.responses.details.MovieDetailApiModel
import com.moviediscovery.api.responses.movies.MovieApiModel
import com.moviediscovery.data.db.MovieEntity
import com.moviediscovery.model.Movie
import com.moviediscovery.model.MovieDetails
import com.moviediscovery.model.MovieType

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.setDataIfAvailable(isVisible: Boolean, execute: () -> Unit) {
    if (isVisible) {
        this.makeVisible()
        execute()
    } else {
        this.makeGone()
    }
}

fun List<MovieApiModel>.toMoviesFromApi() = map {
    Movie(
        id = it.id,
        title = it.title,
        overview = it.overview,
        posterPath = it.posterPath
    )
}

fun List<MovieEntity>.toMoviesFromEntities() = map {
    Movie(
        id = it.movieId,
        title = it.title,
        overview = it.overview,
        posterPath = it.posterPath
    )
}

fun List<Movie>.toEntityMovies(movieType: MovieType) = map {
    MovieEntity(
        movieId = it.id,
        title = it.title,
        overview = it.overview,
        posterPath = it.posterPath,
        movieType = movieType.type
    )
}

fun MovieDetailApiModel.toMovieDetails() = MovieDetails(
    id = this.id,
    title = this.title,
    overview = this.overview,
    popularity = this.popularity,
    movieImageUrl = this.movieImageUrl,
    releaseDate = this.releaseDate,
    tagline = this.tagline,
    voteAverage = this.voteAverage,
    genres = this.genres?.map {
        it.name
    }
)

fun SearchView.addOnQueryTextChangeListener(onQueryTextChanged: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            onQueryTextChanged(query)
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            onQueryTextChanged(newText)
            return true
        }
    })
}

fun ImageView.loadImage(url: String?) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_placeholder_movies)
        .error(R.drawable.ic_placeholder_movies)
        .into(this)
}