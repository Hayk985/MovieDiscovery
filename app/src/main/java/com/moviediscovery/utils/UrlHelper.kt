package com.moviediscovery.utils

object UrlHelper {

    private const val POSTER_URL = "https://image.tmdb.org/t/p/w500"
    private const val BACKDROP_URL = "https://image.tmdb.org/t/p/w1280"

    fun getPosterUrl(posterPath: String?): String? {
        return posterPath?.let { path ->
            if (path.isNotBlank()) {
                POSTER_URL.plus(path)
            } else null
        }
    }

    fun getBackDropUrl(backdropPath: String?): String? {
        return backdropPath?.let { path ->
            if (path.isNotBlank()) {
                BACKDROP_URL.plus(path)
            } else null
        }
    }
}