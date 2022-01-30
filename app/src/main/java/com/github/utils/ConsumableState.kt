package com.github.utils

import org.jetbrains.annotations.NotNull

class ConsumableState<T : Any>
private constructor(private val value: Optional<T>) {

    private var isConsumable = true

    private constructor() : this(Optional.empty())

    fun consume(callback: (T) -> Unit) {
        value.orElse(null)
            ?.takeIf { isConsumable }?.also {
                callback(it)
                isConsumable = false
            }
    }

    fun isPresentAndConsumable() = value.isPresent() && isConsumable

    /**
     * @return The value or null no matter if it's consumed or not
     */
    fun peek(): T? = value.orElse(null)

    companion object {

        fun <T : Any> of(@NotNull value: T): ConsumableState<T> {
            return ConsumableState(Optional.of(value))
        }

        fun <T : Any> of(): ConsumableState<T> {
            return ConsumableState()
        }
    }
}