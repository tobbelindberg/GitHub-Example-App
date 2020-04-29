package com.github.domain.util

import io.reactivex.Observable

/**
 * Extensions that gives us same functionality like rxjava1:s first().
 */
fun <T> Observable<T>.first(): Observable<T> = firstOrError().toObservable()