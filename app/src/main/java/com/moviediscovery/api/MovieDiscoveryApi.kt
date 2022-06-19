package com.moviediscovery.api

import com.moviediscovery.api.responses.details.MovieDetailApiModel
import com.moviediscovery.api.responses.movies.MovieApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDiscoveryApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") pageNumber: Int,
    ): Response<MovieApiResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int,
    ): Response<MovieApiResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") pageNumber: Int,
    ): Response<MovieApiResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") movieName: String,
        @Query("page") pageNumber: Int,
    ): Response<MovieApiResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") movieId: Int,
    ): Response<MovieDetailApiModel>
}