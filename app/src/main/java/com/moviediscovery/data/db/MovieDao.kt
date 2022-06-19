package com.moviediscovery.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies_table WHERE movieType = :movieType")
    suspend fun getMovies(movieType: String): List<MovieEntity>

    @Query("DELETE FROM movies_table WHERE movieType = :movieType")
    suspend fun clearCache(movieType: String)
}