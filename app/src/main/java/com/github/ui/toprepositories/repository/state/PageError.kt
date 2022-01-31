package com.github.ui.toprepositories.repository.state

import com.github.base.state.PartialState

class PageError(private val error: Throwable) : PartialState<RepositoryState> {

    override fun reduceState(previousState: RepositoryState): RepositoryState {

        return previousState.copy(error = error, loading = false, swipeLoading = false)
    }
}