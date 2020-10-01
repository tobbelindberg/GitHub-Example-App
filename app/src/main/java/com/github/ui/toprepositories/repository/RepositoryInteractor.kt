package com.github.ui.toprepositories.repository

import com.github.base.state.PartialState
import com.github.data.repositories.RepositoryRepo
import com.github.domain.model.Repository
import com.github.domain.util.Empty
import com.github.domain.util.first
import com.github.ui.toprepositories.repository.state.PageError
import com.github.ui.toprepositories.repository.state.PageLoaded
import com.github.ui.toprepositories.repository.state.PageLoading
import com.github.ui.toprepositories.repository.state.RepositoryState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RepositoryInteractor
@Inject constructor(
    private val repository: Repository,
    private val repositoryRepo: RepositoryRepo
) {

    private val refresh = PublishSubject.create<Empty>()

    fun stateObservable(): Observable<RepositoryState> {
        return Observable.merge(
            page(),
            refresh()
        )
            .scan(RepositoryState(repository), ::reduce)
    }

    fun reduce(
        previousState: RepositoryState,
        partialState: PartialState<RepositoryState>
    ): RepositoryState {
        return partialState.reduceState(previousState)
    }

    fun onRefresh() {
        refresh.onNext(Empty)
    }

    private fun page(): Observable<PartialState<RepositoryState>> {
        return repositoryRepo.getPullRequests(repository.owner, repository.name).first()
            .subscribeOn(Schedulers.io())
            .map<PartialState<RepositoryState>> { PageLoaded(it) }
            .onErrorReturn { PageError(it) }
            .startWithItem(PageLoading())
    }

    private fun refresh(): Observable<PartialState<RepositoryState>> {
        return refresh.switchMap { _ ->
            repositoryRepo.getPullRequests(repository.owner, repository.name).first()
                .subscribeOn(Schedulers.io())
                .map<PartialState<RepositoryState>> { PageLoaded(it) }
                .onErrorReturn { PageError(it) }
                .startWithItem(PageLoading())
        }
    }

}
