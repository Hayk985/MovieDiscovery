package com.moviediscovery.data.repository

import com.moviediscovery.api.Resource
import com.moviediscovery.api.responses.movies.MovieApiResponse
import com.moviediscovery.app.toEntityMovies
import com.moviediscovery.app.toMoviesFromApi
import com.moviediscovery.app.toMoviesFromEntities
import com.moviediscovery.common.Pagination
import com.moviediscovery.data.repository.local.MovieLocalDataSource
import com.moviediscovery.data.repository.remote.MovieRemoteDataSource
import com.moviediscovery.model.Movie
import com.moviediscovery.model.MovieData
import com.moviediscovery.model.MovieType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

interface MovieRepository {
    suspend fun getMovies(
        movieType: MovieType,
        pageNumber: Int,
        shouldFetchFromCache: Boolean = true,
    ): Flow<Resource<MovieData>>

    suspend fun searchMovies(
        query: String,
        pageNumber: Int,
    ): Flow<Resource<MovieData>>
}

class MovieRepositoryImpl @Inject constructor(
    private var movieLocalDataSource: MovieLocalDataSource,
    private var movieRemoteDataSource: MovieRemoteDataSource,
) : MovieRepository {

    override suspend fun getMovies(
        movieType: MovieType,
        pageNumber: Int,
        shouldFetchFromCache: Boolean,
    ): Flow<Resource<MovieData>> {
        return flow {
            // Fetch from cache only for the first page
            if (pageNumber == Pagination.FIRST_PAGE && shouldFetchFromCache) {
                fetchCachedMovies(movieType) {
                    emit(Resource.Success(MovieData(it)))
                }
            }

            emitAll(fetchMoviesFromApi(movieType, pageNumber))
        }
    }

    override suspend fun searchMovies(
        query: String,
        pageNumber: Int,
    ): Flow<Resource<MovieData>> = flow {
        try {
            val moviesResponse = movieRemoteDataSource.searchMovies(query, pageNumber)
            val movieData = handleApiResponse(moviesResponse)
            if (movieData != null) {
                emit(Resource.Success(movieData))
            } else {
                emit(Resource.Error(null))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }

    private suspend inline fun fetchCachedMovies(
        movieType: MovieType,
        onMoviesFetched: (List<Movie>) -> Unit,
    ) {
        val cachedMovieEntities = movieLocalDataSource.getCachedMovies(movieType)
        if (cachedMovieEntities.isNotEmpty()) {
            val cachedMovies = cachedMovieEntities.toMoviesFromEntities()
            onMoviesFetched.invoke(cachedMovies)
        }
    }

    private suspend fun fetchMoviesFromApi(
        movieType: MovieType,
        pageNumber: Int,
    ): Flow<Resource<MovieData>> = flow {
        try {
            val movieResponse: Response<MovieApiResponse> = when (movieType) {
                MovieType.POPULAR -> {
                    movieRemoteDataSource.getPopularMovies(pageNumber)
                }
                MovieType.UPCOMING -> {
                    movieRemoteDataSource.getUpcomingMovies(pageNumber)
                }
                MovieType.TOP_RATED -> {
                    movieRemoteDataSource.getTopRatedMovies(pageNumber)
                }
            }

            val movieData = handleApiResponse(movieResponse)
            movieData?.let {
                if (pageNumber == Pagination.FIRST_PAGE)
                    updateCache(it.movies, movieType)
                emit(Resource.Success(it))
            } ?: emit(Resource.Error(null))
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }

    private fun handleApiResponse(movieApiResponse: Response<MovieApiResponse>): MovieData? {
        if (movieApiResponse.isSuccessful) {
            movieApiResponse.body()?.let {
                return MovieData(
                    it.movies.toMoviesFromApi(), it.currentPage, it.totalPages
                )
            }
        }

        return null
    }

    private suspend fun updateCache(movies: List<Movie>, movieType: MovieType) {
        movieLocalDataSource.apply {
            clearCache(movieType)
            saveMoviesInCache(movies.toEntityMovies(movieType))
        }
    }
}