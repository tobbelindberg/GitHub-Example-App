package com.github.ui.toprepositories

import android.content.res.Resources
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.github.base.vm.BaseViewModel
import com.github.ui.toprepositories.state.TopRepositoriesState
import com.github.ui.toprepositories.vm.RepositoryItemViewModel
import com.github.utils.loge
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
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

        val newList = mutableListOf<RepositoryItemViewModel>()

        state.topRepositories?.mapTo(newList) { repository ->
            RepositoryItemViewModel(repository, resources)
        }

        items.set(newList)

        loadingVisible.set(state.loading)

        errorVisible.set(state.error != null && !state.loading)
    }

    fun onRefresh() {
        interactor.onRefresh()
    }
}


