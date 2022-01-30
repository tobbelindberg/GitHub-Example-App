package com.github.utils


class Optional<T>(private val value: T?) {

    private constructor() : this(null)

    fun get(): T {
        if (value == null) {
            throw NoSuchElementException("No value present")
        }
        return value
    }

    fun orElse(default: T?): T? {
        return value ?: default
    }
    
    fun isPresent() = value != null

    companion object {

        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }

        fun <T> empty(): Optional<T> {
            return Optional()
        }

    }

}