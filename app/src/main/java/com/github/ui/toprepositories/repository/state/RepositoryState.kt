package com.github.ui.toprepositories.repository.state

import com.github.base.state.State
import com.github.domain.model.PullRequest
import com.github.domain.model.Repository

data class RepositoryState(
    val repository: Repository,
    val pullRequests: List<PullRequest> = emptyList(),
    val error: Throwable? = null,
    val loading: Boolean = false,
    val swipeLoading: Boolean = false
) : State