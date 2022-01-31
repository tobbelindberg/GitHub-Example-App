package com.github.ui.toprepositories.state

import com.github.base.state.State
import com.github.domain.model.Repository
import com.github.utils.ConsumableState

data class TopRepositoriesState(
    val topRepositories: List<Repository> = emptyList(),
    val pageError: Throwable? = null,
    val loading: Boolean = false,
    val swipeLoading: Boolean = false,
    val nextPageLoading: Boolean = false,
    val nextPageError: ConsumableState<Throwable> = ConsumableState.of()
) : State