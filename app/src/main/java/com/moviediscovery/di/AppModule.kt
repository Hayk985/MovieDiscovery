package com.moviediscovery.di

import com.moviediscovery.common.Pagination
import com.moviediscovery.common.PaginationImpl
import com.moviediscovery.data.repository.MovieDetailsRepository
import com.moviediscovery.data.repository.MovieDetailsRepositoryImpl
import com.moviediscovery.data.repository.MovieRepository
import com.moviediscovery.data.repository.MovieRepositoryImpl
import com.moviediscovery.data.repository.local.MovieLocalDataSource
import com.moviediscovery.data.repository.remote.MovieRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieLocalDataSource: MovieLocalDataSource,
        movieRemoteDataSource: MovieRemoteDataSource,
    ): MovieRepository {
        return MovieRepositoryImpl(
            movieLocalDataSource,
            movieRemoteDataSource
        )
    }

    @Provides
    fun provideMovieDetailsRepository(
        movieRemoteDataSource: MovieRemoteDataSource,
    ): MovieDetailsRepository {
        return MovieDetailsRepositoryImpl(
            movieRemoteDataSource
        )
    }

    @Provides
    fun providePagination(): Pagination {
        return PaginationImpl()
    }
}