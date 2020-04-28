package com.github.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.github.base.vm.BaseViewModel
import com.github.di.GenericViewModelFactory
import com.github.utils.simpleName
import javax.inject.Inject


abstract class BaseDialogFragment<VM : BaseViewModel> : AppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<VM>

    protected abstract val viewModel: VM

    fun show(manager: FragmentManager) {
        super.show(manager, simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initStateObservable()
    }


}