package com.github.ui.toprepositories.state

import com.github.data.paging.PagePartialState
import com.github.domain.model.Repository


class NextPageLoading(override val nextPage: List<Repository>? = null, override val nextPageLoading: Boolean = true) : PagePartialState<Repository, TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {

        return previousState.copy(nextPageLoading = nextPageLoading)
    }
}