package com.moviediscovery.api.responses.movies

import com.google.gson.annotations.SerializedName

data class MovieApiModel(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
)