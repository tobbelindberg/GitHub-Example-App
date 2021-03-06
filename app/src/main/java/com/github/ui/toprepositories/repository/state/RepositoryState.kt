package com.github.ui.toprepositories.repository.state

import com.github.base.state.State
import com.github.domain.model.PullRequest
import com.github.domain.model.Repository

data class RepositoryState(
    val repository: Repository,
    val pullRequests: List<PullRequest>? = null,
    val error: Throwable? = null,
    val loading: Boolean = false
) : State