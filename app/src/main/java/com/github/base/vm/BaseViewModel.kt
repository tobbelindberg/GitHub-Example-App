package com.github.base.vm

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val subscriptions = CompositeDisposable()

    private var isInitialized = false

    fun initStateObservable() {
        val initializeSubscriptions = !isInitialized
        if (initializeSubscriptions) {
            initializeSubscriptions()
        } else {
            restoreToInitialState()
        }
        isInitialized = true
    }

    /**
     * Override this method if you wish to initialize some subscriptions.
     *
     * Called when the fragment/activity gets created and subscriptions has to be initialized.
     *
     */
    protected open fun initializeSubscriptions() {

    }

    /**
     * Override this method if you wish to reset some state during recreation of the fragment/activity
     * while the subscriptions remain initialized.
     *
     * Called when the fragment/activity gets re-created but the ViewModel and its subscriptions has
     * been retained.
     *
     */
    protected open fun restoreToInitialState() {

    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}