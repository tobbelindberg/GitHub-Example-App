package com.github.ui.toprepositories.state

import com.github.base.state.PartialState


class PageLoading : PartialState<TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {

        return previousState.copy(loading = true)
    }
}