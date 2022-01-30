package com.github.ui.toprepositories.state

import com.github.data.paging.PagePartialState
import com.github.domain.model.Repository
import com.github.utils.ConsumableState

class NextPageError(private val error: Throwable, override val nextPage: List<Repository>? = null, override val nextPageLoading: Boolean = false) : PagePartialState<Repository, TopRepositoriesState> {

    override fun reduceState(previousState: TopRepositoriesState): TopRepositoriesState {

        return previousState.copy(nextPageError = ConsumableState.of(error), nextPageLoading = nextPageLoading)
    }
}