package com.github.domain.model

import java.util.*

data class PullRequest(
    val id: Long,
    val title: String,
    val number: Int,
    val userName: String,
    val updatedAt: Date,
    val state: State
) {

    enum class State {
        OPEN,
        CLOSED
    }
}