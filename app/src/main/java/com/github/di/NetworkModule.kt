package com.github.di

import com.github.data.services.RepositoryNetworkService
import com.github.domain.services.RepositoryService
import dagger.Binds
import dagger.Module
import dagger.Reusable

/**
 * Provides Network services.
 */
@Module(includes = [ObjectMapperModule::class])
abstract class NetworkModule {

    @Binds
    @Reusable
    abstract fun provideRepositoryService(service: RepositoryNetworkService): RepositoryService
}