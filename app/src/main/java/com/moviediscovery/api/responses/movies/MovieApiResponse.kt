package com.moviediscovery.api.responses.movies

import com.google.gson.annotations.SerializedName

data class MovieApiResponse(
    @SerializedName("results") val movies: List<MovieApiModel> = listOf(),
    @SerializedName("page") val currentPage: Int,
    @SerializedName("total_pages") val totalPages: Int,
)