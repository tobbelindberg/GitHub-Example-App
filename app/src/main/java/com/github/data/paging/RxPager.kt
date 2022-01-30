package com.github.data.paging

import com.github.base.state.State
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class RxPager<E, T : State>(private val initialNextPageToLoad: Int, private val pageSize: Int, onLoadNextPage: (Int) -> Observable<PagePartialState<E, T>>) {
    private val pages = Pages(nextPageToLoad = initialNextPageToLoad)


    private val pageSubject = PublishSubject.create<Int>().toSerialized()
    private var pageLoading = false

    val observable: Observable<PagePartialState<E, T>> = pageSubject.hide()
        .observeOn(Schedulers.io())
        .filter { !pageLoading }
        .doOnNext {
            pageLoading = true
        }
        .switchMap(onLoadNextPage)
        .doOnNext { partialPageState ->
            partialPageState.nextPage?.also { page ->
                pages.hasMorePages = page.isNotEmpty() && page.size >= pageSize
                pages.nextPageToLoad++
            }
        }
        .doOnNext {
            pageLoading = it.nextPageLoading
        }

    fun next() {
        if (pageSubject.hasObservers() && pages.hasMorePages) {
            if (!pageLoading) {
                pageSubject.onNext(pages.nextPageToLoad)
            }
        }
    }


    fun reset() {
        pages.nextPageToLoad = initialNextPageToLoad
        pages.hasMorePages = true
        pageLoading = false
    }
}