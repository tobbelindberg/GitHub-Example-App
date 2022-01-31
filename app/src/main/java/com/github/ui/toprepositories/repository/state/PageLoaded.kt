package com.github.ui.toprepositories.repository.state

import com.github.base.state.PartialState
import com.github.domain.model.PullRequest


class PageLoaded(private val pullRequests: List<PullRequest>) :
    PartialState<RepositoryState> {

    override fun reduceState(previousState: RepositoryState): RepositoryState {

        return previousState.copy(pullRequests = pullRequests, loading = false, swipeLoading = false, error = null)
    }
}