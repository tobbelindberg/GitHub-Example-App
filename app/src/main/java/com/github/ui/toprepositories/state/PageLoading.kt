package com.github.ui.toprepositories.state

import com.github.base.state.PartialState

class PageLoading(private val swipeRefreshing: Boolean) : PartialState<TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {
        val loading = true.takeIf { !swipeRefreshing } ?: previousState.loading
        val swipeLoading = true.takeIf { swipeRefreshing } ?: previousState.swipeLoading

        return previousState.copy(loading = loading, swipeLoading = swipeLoading)
    }
}