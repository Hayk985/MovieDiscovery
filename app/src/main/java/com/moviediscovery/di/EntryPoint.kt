package com.moviediscovery.di

import com.moviediscovery.api.MovieDiscoveryApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface EntryPoint {
    fun getMovieDiscoveryApi(): MovieDiscoveryApi
}