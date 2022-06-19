package com.moviediscovery.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moviediscovery.utils.Constants

@Entity(tableName = Constants.MOVIES_TABLE)
data class MovieEntity(
    val movieId: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val movieType: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)