package com.moviediscovery.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String?,
    val popularity: Double?,
    val movieImageUrl: String?,
    val releaseDate: String?,
    val tagline: String?,
    val voteAverage: Double?,
    val genres: List<String>?,
)