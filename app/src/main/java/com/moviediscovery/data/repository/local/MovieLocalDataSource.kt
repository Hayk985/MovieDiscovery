package com.moviediscovery.data.repository.local

import com.moviediscovery.data.db.MovieDao
import com.moviediscovery.data.db.MovieEntity
import com.moviediscovery.model.MovieType
import javax.inject.Inject

interface MovieLocalDataSource {
    suspend fun saveMoviesInCache(movies: List<MovieEntity>)
    suspend fun getCachedMovies(movieType: MovieType): List<MovieEntity>
    suspend fun clearCache(movieType: MovieType)
}

class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
) : MovieLocalDataSource {

    override suspend fun saveMoviesInCache(movies: List<MovieEntity>) {
        movieDao.cacheMovies(movies)
    }

    override suspend fun getCachedMovies(movieType: MovieType): List<MovieEntity> =
        movieDao.getMovies(movieType.type)

    override suspend fun clearCache(movieType: MovieType) {
        movieDao.clearCache(movieType.type)
    }
}