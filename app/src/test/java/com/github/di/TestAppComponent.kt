package com.github.di

import com.github.ui.toprepositories.TopRepositoriesViewModelTest
import com.github.ui.toprepositories.repository.RepositoryViewModelTestComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Main Test component that brings all the dagger stuff to life.
 */
@Singleton
@Component(modules = [AppModule::class])
interface TestAppComponent : AppComponent {

    fun inject(topRepositoriesViewModelTest: TopRepositoriesViewModelTest)

    fun repositoryViewModelTestBuilder(): RepositoryViewModelTestComponent.Builder

}