package com.github.di

import com.github.ui.MainActivity
import com.github.ui.toprepositories.TopRepositoriesComponent
import com.github.ui.toprepositories.repository.RepositoryComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Main component that brings all the dagger stuff to life.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun topRepositoriesBuilder(): TopRepositoriesComponent.Builder

    fun repositoryBuilder(): RepositoryComponent.Builder

}