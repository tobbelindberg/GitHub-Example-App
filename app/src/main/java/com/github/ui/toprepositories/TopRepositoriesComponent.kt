package com.github.ui.toprepositories

import com.github.di.scope.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface TopRepositoriesComponent {

    fun inject(topRepositoriesFragment: TopRepositoriesFragment)

    @Subcomponent.Builder
    interface Builder {

        fun build(): TopRepositoriesComponent
    }
}