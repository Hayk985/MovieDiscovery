package com.moviediscovery.data.db

import com.moviediscovery.model.MovieType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class MovieDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testMovieDatabase")
    lateinit var movieDatabase: MovieDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        hiltRule.inject()
        movieDao = movieDatabase.movieDao()
    }

    @Test
    fun cacheMovies() = runBlocking {
        val movieEntity = MovieEntity(
            movieId = 1321,
            title = "Spider man",
            overview = "Spider man description",
            posterPath = "No poster",
            movieType = MovieType.POPULAR.type,
            id = 1)

        movieDao.cacheMovies(mutableListOf(movieEntity))
        val cachedMovies = movieDao.getMovies(MovieType.POPULAR.type)
        assertEquals(true, cachedMovies.contains(movieEntity))
    }

    @Test
    fun clearCache() {
        val movieEntity = MovieEntity(
            movieId = 1321,
            title = "Spider man",
            overview = "Spider man description",
            posterPath = "No poster",
            movieType = MovieType.POPULAR.type,
            id = 1)

        runBlocking {
            movieDao.cacheMovies(mutableListOf(movieEntity))
            movieDao.clearCache(MovieType.POPULAR.type)
            val cachedMovies = movieDao.getMovies(MovieType.POPULAR.type)
            assertEquals(true, cachedMovies.isEmpty())
        }
    }

    @After
    fun teardown() {
        movieDatabase.close()
    }
}