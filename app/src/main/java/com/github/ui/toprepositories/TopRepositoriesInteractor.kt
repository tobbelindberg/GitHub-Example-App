package com.github.ui.toprepositories

import com.github.base.state.PartialState
import com.github.data.paging.PagePartialState
import com.github.data.paging.RxPager
import com.github.data.repositories.RepositoryRepo
import com.github.domain.model.Repository
import com.github.domain.util.first
import com.github.ui.toprepositories.state.*
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class TopRepositoriesInteractor
@Inject constructor(
    private val repositoryRepo: RepositoryRepo
) {

    private companion object {
        private const val INITIAL_PAGE = 1
        private const val PAGE_SIZE = 20
    }

    private val refresh = PublishSubject.create<Boolean>()
    private val pager = RxPager(INITIAL_PAGE + 1, PAGE_SIZE, ::nextPageObservable)

    fun stateObservable(): Observable<TopRepositoriesState> {
        return Observable.merge(
            page(),
            refresh(),
            nextPage()
        )
            .scan(TopRepositoriesState(), ::reduce)
    }

    fun reduce(
        previousState: TopRepositoriesState,
        partialState: PartialState<TopRepositoriesState>
    ): TopRepositoriesState {
        return partialState.reduceState(previousState)
    }

    fun onRefresh(swipeRefreshing: Boolean) {
        refresh.onNext(swipeRefreshing)
    }

    private fun page(swipeRefreshing: Boolean = false): Observable<PartialState<TopRepositoriesState>> {
        return repositoryRepo.getTopRepositories(INITIAL_PAGE, PAGE_SIZE).first()
            .subscribeOn(Schedulers.io())
            .doOnNext { pager.reset() }
            .map<PartialState<TopRepositoriesState>> { PageLoaded(it) }
            .onErrorReturn { PageError(it) }
            .startWithItem(PageLoading(swipeRefreshing))
    }

    private fun refresh(): Observable<PartialState<TopRepositoriesState>> {
        return refresh.switchMap { swipeRefreshing ->
            page(swipeRefreshing)
        }
    }

    fun onLoadNextPage() {
        pager.next()
    }

    private fun nextPage(): @NonNull Observable<out PartialState<TopRepositoriesState>> {
        return pager.observable

    }

    private fun nextPageObservable(page: Int): Observable<PagePartialState<Repository, TopRepositoriesState>> {
        return repositoryRepo.getTopRepositories(page, PAGE_SIZE).first()
            .subscribeOn(Schedulers.io())
            .map<PagePartialState<Repository, TopRepositoriesState>> { NextPageLoaded(it) }
            .onErrorReturn { NextPageError(it) }
            .startWithItem(NextPageLoading())
    }

}
