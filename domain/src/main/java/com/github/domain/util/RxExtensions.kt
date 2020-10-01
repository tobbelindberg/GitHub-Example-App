package com.github.domain.util

import io.reactivex.rxjava3.core.Observable

/**
 * Extensions that gives us same functionality like rxjava1:s first().
 */
fun <T> Observable<T>.first(): Observable<T> = firstOrError().toObservable()