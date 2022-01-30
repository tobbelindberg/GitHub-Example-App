package com.github.data.paging

import com.github.base.state.PartialState
import com.github.base.state.State

interface PagePartialState<E, T : State> : PartialState<T> {
    val nextPage: List<E>?
    val nextPageLoading: Boolean
}