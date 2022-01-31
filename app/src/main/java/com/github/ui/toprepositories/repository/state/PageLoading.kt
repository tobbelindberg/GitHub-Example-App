package com.github.ui.toprepositories.repository.state

import com.github.base.state.PartialState


class PageLoading(private val swipeRefreshing: Boolean) : PartialState<RepositoryState> {

    override fun reduceState(previousState: RepositoryState): RepositoryState {
        val loading = true.takeIf { !swipeRefreshing } ?: previousState.loading
        val swipeLoading = true.takeIf { swipeRefreshing } ?: previousState.swipeLoading

        return previousState.copy(loading = loading, swipeLoading = swipeLoading)
    }
}