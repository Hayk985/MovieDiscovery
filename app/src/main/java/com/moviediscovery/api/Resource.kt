package com.moviediscovery.api

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val exception: Exception?) : Resource<T>()
}