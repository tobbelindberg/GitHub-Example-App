package com.github.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("itemDecoration")
fun RecyclerView.setItemDecoration(
    oldDecoration: RecyclerView.ItemDecoration?,
    newDecoration: RecyclerView.ItemDecoration?
) {
    oldDecoration?.also { oldDecoration ->
        removeItemDecoration(oldDecoration)
    }
    newDecoration?.also { newDecoration ->
        addItemDecoration(newDecoration)
    }

}