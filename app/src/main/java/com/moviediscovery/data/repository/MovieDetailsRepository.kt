package com.moviediscovery.data.repository

import com.moviediscovery.api.Resource
import com.moviediscovery.app.toMovieDetails
import com.moviediscovery.data.repository.remote.MovieRemoteDataSource
import com.moviediscovery.model.MovieDetails
import javax.inject.Inject

interface MovieDetailsRepository {
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails>
}

class MovieDetailsRepositoryImpl @Inject constructor(
    private var movieRemoteDataSource: MovieRemoteDataSource,
) : MovieDetailsRepository {

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails> {
        try {
            val movieDetailsApiResponse = movieRemoteDataSource.getMovieDetails(movieId)
            if (movieDetailsApiResponse.isSuccessful)
                movieDetailsApiResponse.body()?.let {
                    return Resource.Success(it.toMovieDetails())
                }

            return Resource.Error(null)
        } catch (exception: Exception) {
            return Resource.Error(exception)
        }
    }
}