package com.github.ui.toprepositories.state

import com.github.domain.model.Repository
import com.github.utils.paging.PagePartialState

class NextPageLoading(override val nextPage: List<Repository>? = null, override val nextPageLoading: Boolean = true) : PagePartialState<Repository, TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {

        return previousState.copy(nextPageLoading = nextPageLoading)
    }
}