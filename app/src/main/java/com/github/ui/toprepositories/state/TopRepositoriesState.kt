package com.github.ui.toprepositories.state

import com.github.base.state.State
import com.github.domain.model.Repository

data class TopRepositoriesState(
    val topRepositories: List<Repository> = emptyList(),
    val error: Throwable? = null,
    val loading: Boolean = false,
    val nextPageLoading: Boolean = false
) : State