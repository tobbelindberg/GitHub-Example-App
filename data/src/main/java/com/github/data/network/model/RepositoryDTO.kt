package com.github.data.network.model

import java.util.*

data class RepositoryDTO(
    val id: Long,
    val name: String,
    val owner: UserDTO,
    val stargazers_count: Int,
    val forks_count: Int,
    val watchers_count: Int,
    val open_issues_count: Int,
    val language: String?,
    val updated_at: Date
)