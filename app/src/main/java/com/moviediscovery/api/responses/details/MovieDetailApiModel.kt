package com.moviediscovery.api.responses.details

import com.google.gson.annotations.SerializedName

data class MovieDetailApiModel(
    @SerializedName("backdrop_path") val movieImageUrl: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    val genres: List<Genre>?,
    val id: Int,
    val title: String,
    val overview: String?,
    val popularity: Double?,
    val tagline: String?,
)

data class Genre(
    val name: String,
)