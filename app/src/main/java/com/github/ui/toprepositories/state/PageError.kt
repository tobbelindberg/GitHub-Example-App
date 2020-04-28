package com.github.ui.toprepositories.state

import com.github.base.state.PartialState

class PageError(private val error: Throwable) : PartialState<TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {

        return previousState.copy(error = error, loading = false)
    }
}