package com.github.ui.toprepositories

import com.github.base.state.PartialState
import com.github.data.repositories.RepositoryRepo
import com.github.domain.util.Empty
import com.github.domain.util.first
import com.github.ui.toprepositories.state.PageError
import com.github.ui.toprepositories.state.PageLoaded
import com.github.ui.toprepositories.state.PageLoading
import com.github.ui.toprepositories.state.TopRepositoriesState
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class TopRepositoriesInteractor
@Inject constructor(
    private val repositoryRepo: RepositoryRepo
) {

    private val refresh = PublishSubject.create<Empty>()

    fun stateObservable(): Observable<TopRepositoriesState> {
        return Observable.merge(
            page(),
            refresh()
        )
            .scan(TopRepositoriesState(), ::reduce)
    }

    fun reduce(
        previousState: TopRepositoriesState,
        partialState: PartialState<TopRepositoriesState>
    ): TopRepositoriesState {
        return partialState.reduceState(previousState)
    }

    fun onRefresh() {
        refresh.onNext(Empty)
    }

    private fun page(): Observable<PartialState<TopRepositoriesState>> {
        return repositoryRepo.getTopRepositories().first()
            .subscribeOn(Schedulers.io())
            .map<PartialState<TopRepositoriesState>> { PageLoaded(it) }
            .onErrorReturn { PageError(it) }
            .startWith(PageLoading())
    }

    private fun refresh(): Observable<PartialState<TopRepositoriesState>> {
        return refresh.switchMap { _ ->
            repositoryRepo.getTopRepositories().first()
                .subscribeOn(Schedulers.io())
                .map<PartialState<TopRepositoriesState>> { PageLoaded(it) }
                .onErrorReturn { PageError(it) }
                .startWith(PageLoading())
        }
    }

}
