package com.github.utils

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @param activityScoped Set to true if it should be retained as long as the activity is alive,
 * false if it should be retained as long as the fragment is alive.
 */
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    crossinline getViewModelFactory: () -> ViewModelProvider.Factory,
    activityScoped: Boolean,
    crossinline getKey: () -> String? = { null }
) =
    lazy {
        val viewModelFactory = getViewModelFactory()
        val key = getKey()
        val provider = if (activityScoped) {
            ViewModelProvider(requireActivity(), viewModelFactory)
        } else {
            ViewModelProvider(this, viewModelFactory)
        }
        if (key == null) provider[VM::class.java] else provider.get(
            VM::class.java.name + key,
            VM::class.java
        )
    }

inline fun <reified BINDING : ViewDataBinding> Fragment.bindingProvider(@LayoutRes layoutId: Int) =
    lazy {
        DataBindingUtil.inflate<BINDING>(layoutInflater, layoutId, null, false)
    }