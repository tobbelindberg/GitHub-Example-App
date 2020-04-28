package com.github.ui.toprepositories.state

import com.github.base.state.PartialState
import com.github.domain.model.Repository


class PageLoaded(private val topRepositories: List<Repository>) :
    PartialState<TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {

        return previousState.copy(topRepositories = topRepositories, loading = false, error = null)
    }
}