package com.github.ui.toprepositories.repository

import android.content.res.Resources
import android.text.format.DateFormat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.github.R
import com.github.base.vm.BaseViewModel
import com.github.ui.toprepositories.repository.state.RepositoryState
import com.github.ui.toprepositories.repository.vm.PullRequestItemViewModel
import com.github.utils.loge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RepositoryViewModel
@Inject constructor(
    private val interactor: RepositoryInteractor,
    private val resources: Resources
) : BaseViewModel() {

    val title = ObservableField<String>()

    val owner = ObservableField<String>()
    val updatedAt = ObservableField<String>()

    val starCount = ObservableField<String>()
    val forksCount = ObservableField<String>()
    val watcherCount = ObservableField<String>()
    val openIssuesCount = ObservableField<String>()
    val language = ObservableField<String>()

    val errorVisible = ObservableBoolean(false)
    val loadingVisible = ObservableBoolean(false)
    val swipeLoading = ObservableBoolean(false)

    val items = ObservableField<List<PullRequestItemViewModel>>(listOf())

    override fun initializeSubscriptions() {
        interactor.stateObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = ::render, onError = { it.loge() })
            .addTo(subscriptions)
    }

    private fun render(state: RepositoryState) {
        if (state.error != null) {
            state.error.loge()
        }

        title.set(state.repository.name)

        owner.set(state.repository.owner)
        updatedAt.set(
            resources.getString(
                R.string.last_updated,
                DateFormat.format("MMM d, yyyy HH:mm", state.repository.updatedAt)
            )
        )

        starCount.set(state.repository.starCount.toString())
        forksCount.set(state.repository.forksCount.toString())
        watcherCount.set(state.repository.watchersCount.toString())
        openIssuesCount.set(state.repository.openIssuesCount.toString())
        language.set(state.repository.language)

        items.set(state.pullRequests.map { pullRequest ->
            PullRequestItemViewModel(pullRequest, resources)
        })

        loadingVisible.set(state.loading)

        swipeLoading.set(state.swipeLoading)

        errorVisible.set(state.error != null && !state.loading)
    }

    fun onRefresh(swipeRefreshing: Boolean = false) {
        interactor.onRefresh(swipeRefreshing)
    }
}


