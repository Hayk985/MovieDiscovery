package com.moviediscovery.model

data class MovieData(
    val movies: List<Movie>,
    val currentPage: Int = 1,
    val totalPages: Int = 500,
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
)
