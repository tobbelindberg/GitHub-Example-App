package com.github.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.base.vm.BaseViewModel
import com.github.di.GenericViewModelFactory
import com.github.ui.Navigator
import javax.inject.Inject


abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    var navigator: Navigator? = null

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<VM>

    protected abstract val viewModel: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireActivity() is Navigator) {
            navigator = requireActivity() as Navigator
        } else {
            throw RuntimeException("${requireActivity()} must implement Navigator")
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initStateObservable()
    }

}