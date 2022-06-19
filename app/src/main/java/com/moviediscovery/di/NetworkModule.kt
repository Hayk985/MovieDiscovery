package com.moviediscovery.di

import com.moviediscovery.BuildConfig
import com.moviediscovery.api.MovieDiscoveryApi
import com.moviediscovery.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val OKHTTP_TIME = 15L
    private const val PARAM_API_KEY = "api_key"

    @Provides
    @Singleton
    fun provideMovieDiscoveryApi(okHttpClient: OkHttpClient): MovieDiscoveryApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MovieDiscoveryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpClientInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(OKHTTP_TIME, TimeUnit.SECONDS)
            .readTimeout(OKHTTP_TIME, TimeUnit.SECONDS)
            .addInterceptor(httpClientInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClientInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val url = originalRequest.url().newBuilder()
                .addQueryParameter(
                    PARAM_API_KEY, BuildConfig.API_KEY,
                ).build()
            val request = originalRequest.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }
}