package com.github.ui.toprepositories

import android.content.res.Resources
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.base.vm.BaseViewModel
import com.github.base.vm.ItemViewModel
import com.github.base.vm.LoadingFooterItemViewModel
import com.github.ui.toprepositories.state.TopRepositoriesState
import com.github.ui.toprepositories.vm.RepositoryItemViewModel
import com.github.utils.ConsumableState
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
    val swipeLoading = ObservableBoolean(false)
    val items = ObservableField<List<ItemViewModel>>(listOf())
    private val paginationError = MutableLiveData<ConsumableState<Throwable>>()
    val paginationErrorData: LiveData<ConsumableState<Throwable>>
        get() = paginationError

    override fun initializeSubscriptions() {
        interactor.stateObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = ::render, onError = { it.loge() })
            .addTo(subscriptions)
    }

    private fun render(state: TopRepositoriesState) {
        if (state.pageError != null) {
            state.pageError.loge()
        }

        val repositories = mutableListOf<ItemViewModel>()
        state.topRepositories.mapTo(repositories) { repository ->
            RepositoryItemViewModel(repository, resources)
        }
        if (state.nextPageLoading) {
            repositories.add(LoadingFooterItemViewModel)
        }

        items.set(repositories)

        loadingVisible.set(state.loading)

        swipeLoading.set(state.swipeLoading)

        errorVisible.set(state.pageError != null && !state.loading)

        state.nextPageError.consume {
            paginationError.value = ConsumableState.of(it)
        }
    }

    fun onRefresh(swipeRefreshing: Boolean = false) {
        interactor.onRefresh(swipeRefreshing)
    }

    fun onLoadNextPage() {
        interactor.onLoadNextPage()
    }
}


