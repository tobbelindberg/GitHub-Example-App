package com.github.ui.toprepositories.repository

import com.github.di.scope.FragmentScope
import com.github.domain.model.Repository
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface RepositoryViewModelTestComponent {

    fun inject(postDetailViewModelTest: RepositoryViewModelTest)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setRepository(repository: Repository): Builder

        fun build(): RepositoryViewModelTestComponent

    }

}