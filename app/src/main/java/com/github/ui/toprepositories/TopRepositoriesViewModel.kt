package com.github.ui.toprepositories

import android.content.res.Resources
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.github.base.vm.BaseViewModel
import com.github.ui.toprepositories.state.TopRepositoriesState
import com.github.ui.toprepositories.vm.RepositoryItemViewModel
import com.github.utils.loge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TopRepositoriesViewModel
@Inject constructor(
    private val interactor: TopRepositoriesInteractor,
    private val resources: Resources
) : BaseViewModel() {

    val errorVisible = ObservableBoolean(false)
    val loadingVisible = ObservableBoolean(false)
    val items = ObservableField<List<RepositoryItemViewModel>>(listOf())

    override fun initializeSubscriptions() {
        interactor.stateObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = ::render, onError = { it.loge() })
            .addTo(subscriptions)
    }

    private fun render(state: TopRepositoriesState) {
        if (state.error != null) {
            state.error.loge()
        }

        items.set(state.topRepositories.map { repository ->
            RepositoryItemViewModel(repository, resources)
        })

        loadingVisible.set(state.loading)

        errorVisible.set(state.error != null && !state.loading)
    }

    fun onRefresh() {
        interactor.onRefresh()
    }

    fun onLoadNextPage() {
        interactor.onLoadNextPage()
    }
}


