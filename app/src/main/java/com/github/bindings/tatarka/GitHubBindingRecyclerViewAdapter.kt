package com.github.bindings.tatarka

import androidx.recyclerview.widget.RecyclerView
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

class GitHubBindingRecyclerViewAdapter<T> : BindingRecyclerViewAdapter<T>() {

    interface OnAdapterCountChangedListener {

        fun onAdapterCountChanged(count: Int)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        registerAdapterDataObserver(adapterDataObserver)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        unregisterAdapterDataObserver(adapterDataObserver)
    }

    private var onAdapterCountChangedListener: OnAdapterCountChangedListener? = null

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            onAdapterCountChangedListener?.onAdapterCountChanged(this@GitHubBindingRecyclerViewAdapter.itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            onAdapterCountChangedListener?.onAdapterCountChanged(this@GitHubBindingRecyclerViewAdapter.itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onAdapterCountChangedListener?.onAdapterCountChanged(this@GitHubBindingRecyclerViewAdapter.itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onAdapterCountChangedListener?.onAdapterCountChanged(this@GitHubBindingRecyclerViewAdapter.itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onAdapterCountChangedListener?.onAdapterCountChanged(this@GitHubBindingRecyclerViewAdapter.itemCount)
        }
    }

    fun setOnAdapterCountChangedCallback(onAdapterCountChangedListener: OnAdapterCountChangedListener) {
        this.onAdapterCountChangedListener = onAdapterCountChangedListener
    }
}