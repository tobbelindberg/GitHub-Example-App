package com.github.di

import com.github.data.mapper.PullRequestMapper
import com.github.data.mapper.RepositoryMapper
import com.github.data.network.model.PullRequestDTO
import com.github.data.network.model.RepositoryDTO
import com.github.domain.mapper.ObjectMapper
import com.github.domain.model.PullRequest
import com.github.domain.model.Repository
import dagger.Binds
import dagger.Module
import dagger.Reusable

/**
 * Provides object mappers mapping network models to domain models.
 */
@Module
abstract class ObjectMapperModule {


    @Binds
    @Reusable
    abstract fun provideRepositoryMapper(repositoryMapper: RepositoryMapper): ObjectMapper<RepositoryDTO, Repository>

    @Binds
    @Reusable
    abstract fun providePullRequestyMapper(pullRequestMapper: PullRequestMapper): ObjectMapper<PullRequestDTO, PullRequest>

}