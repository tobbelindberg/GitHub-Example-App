package com.github.ui.toprepositories.repository.state

import com.github.base.state.PartialState


class PageLoading : PartialState<RepositoryState> {

    override fun reduceState(previousState: RepositoryState): RepositoryState {

        return previousState.copy(loading = true)
    }
}