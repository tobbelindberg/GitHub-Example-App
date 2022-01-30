package com.github.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.data.paging.PagingScrollListener

@BindingAdapter("itemDecoration")
fun RecyclerView.setItemDecoration(
    oldDecoration: RecyclerView.ItemDecoration?,
    newDecoration: RecyclerView.ItemDecoration?
) {
    oldDecoration?.also {
        removeItemDecoration(it)
    }
    newDecoration?.also {
        addItemDecoration(it)
    }
}

@BindingAdapter("onScrollListener")
fun RecyclerView.setOnScrollListener(
    oldScrollListener: PagingScrollListener?,
    newScrollListener: PagingScrollListener?
) {
    oldScrollListener?.also {
        removeOnScrollListener(it)
    }

    newScrollListener?.also {
        addOnScrollListener(it)
    }
}
