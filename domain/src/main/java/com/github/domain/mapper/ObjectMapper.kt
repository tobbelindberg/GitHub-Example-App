package com.github.domain.mapper

interface ObjectMapper<T, U> {
    fun map(model: T): U
}