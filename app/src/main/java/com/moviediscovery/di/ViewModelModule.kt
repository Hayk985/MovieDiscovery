package com.moviediscovery.di

import com.moviediscovery.common.Pagination
import com.moviediscovery.common.PaginationImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun providePagination(
        paginationImpl: PaginationImpl,
    ): Pagination
}