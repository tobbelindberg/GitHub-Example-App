package com.github.data.network.model

import java.util.*

data class PullRequestDTO(
    val id: Long,
    val title: String,
    val number: Int,
    val user: UserDTO,
    val updated_at: Date,
    val state: State
) {

    enum class State {
        open,
        closed
    }
}