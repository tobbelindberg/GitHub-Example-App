package com.github.domain.model

import java.util.*

data class Repository(
    val id: Long,
    val name: String,
    val owner: String,
    val starCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val openIssuesCount: Int,
    val language: String?,
    val updatedAt: Date
)