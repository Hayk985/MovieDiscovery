package com.moviediscovery.data.repository.remote

import com.moviediscovery.api.MovieDiscoveryApi
import com.moviediscovery.api.responses.details.MovieDetailApiModel
import com.moviediscovery.api.responses.movies.MovieApiResponse
import com.moviediscovery.app.MovieApp
import com.moviediscovery.di.EntryPoint
import dagger.hilt.android.EntryPointAccessors
import retrofit2.Response
import javax.inject.Inject

interface MovieRemoteDataSource {
    suspend fun getPopularMovies(pageNumber: Int): Response<MovieApiResponse>
    suspend fun getTopRatedMovies(pageNumber: Int): Response<MovieApiResponse>
    suspend fun getUpcomingMovies(pageNumber: Int): Response<MovieApiResponse>
    suspend fun searchMovies(query: String, pageNumber: Int): Response<MovieApiResponse>
    suspend fun getMovieDetails(movieId: Int): Response<MovieDetailApiModel>
}

class MovieRemoteDataSourceImpl @Inject constructor() : MovieRemoteDataSource {

    /*
    A demonstration of how to inject fields out of Activity/Fragments using EntryPoint
     */
    private val entryPoint = EntryPointAccessors
        .fromApplication(MovieApp.getInstance(), EntryPoint::class.java)

    private var movieDiscoveryApi: MovieDiscoveryApi = entryPoint.getMovieDiscoveryApi()

    override suspend fun getPopularMovies(pageNumber: Int): Response<MovieApiResponse> =
        movieDiscoveryApi.getPopularMovies(pageNumber)

    override suspend fun getTopRatedMovies(pageNumber: Int): Response<MovieApiResponse> =
        movieDiscoveryApi.getTopRatedMovies(pageNumber)

    override suspend fun getUpcomingMovies(pageNumber: Int): Response<MovieApiResponse> =
        movieDiscoveryApi.getUpcomingMovies(pageNumber)

    override suspend fun searchMovies(query: String, pageNumber: Int): Response<MovieApiResponse> =
        movieDiscoveryApi.searchMovie(query, pageNumber)

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetailApiModel> =
        movieDiscoveryApi.getMovieDetails(movieId)
}