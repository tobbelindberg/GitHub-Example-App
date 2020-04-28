package com.github.ui.toprepositories.state

import com.github.base.state.State
import com.github.domain.model.Repository

data class TopRepositoriesState(
    val topRepositories: List<Repository>? = null,
    val error: Throwable? = null,
    val loading: Boolean = false
) : State