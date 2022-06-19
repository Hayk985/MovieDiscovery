package com.moviediscovery.di

import com.moviediscovery.data.repository.local.MovieLocalDataSource
import com.moviediscovery.data.repository.local.MovieLocalDataSourceImpl
import com.moviediscovery.data.repository.remote.MovieRemoteDataSource
import com.moviediscovery.data.repository.remote.MovieRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * All dataSources are provided here
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMovieLocalDataSource(
        movieLocalDataSourceImpl: MovieLocalDataSourceImpl,
    ): MovieLocalDataSource

    @Binds
    @Singleton
    abstract fun bindMovieRemoteDataSource(
        movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl,
    ): MovieRemoteDataSource
}