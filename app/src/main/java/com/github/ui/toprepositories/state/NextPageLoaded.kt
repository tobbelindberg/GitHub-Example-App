package com.github.ui.toprepositories.state

import com.github.domain.model.Repository
import com.github.utils.paging.PagePartialState
import okhttp3.internal.toImmutableList


class NextPageLoaded(override val nextPage: List<Repository>?, override val nextPageLoading: Boolean = false) :
    PagePartialState<Repository, TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {
        val nextPage = nextPage ?: emptyList()

        return previousState.copy(topRepositories = previousState.topRepositories.toImmutableList().plus(nextPage), nextPageLoading = nextPageLoading)
    }
}