package com.github.ui.toprepositories.state

import com.github.domain.model.Repository
import com.github.data.paging.PagePartialState
import okhttp3.internal.toImmutableList


class NextPageLoaded(override val page: List<Repository>) :
    PagePartialState<Repository, TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {

        return previousState.copy(topRepositories = previousState.topRepositories.toImmutableList().plus(page), loading = false, error = null)
    }
}