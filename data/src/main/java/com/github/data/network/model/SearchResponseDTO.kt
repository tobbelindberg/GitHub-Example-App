package com.github.data.network.model

data class SearchResponseDTO(val total_count: Long, val items: List<RepositoryDTO>)