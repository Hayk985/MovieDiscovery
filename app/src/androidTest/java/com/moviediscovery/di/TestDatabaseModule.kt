package com.moviediscovery.di

import android.content.Context
import androidx.room.Room
import com.moviediscovery.data.db.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestDatabaseModule {

    @Provides
    @Singleton
    @Named("testMovieDatabase")
    fun provideInMemoryDb(@ApplicationContext context: Context): MovieDatabase {
        return Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}