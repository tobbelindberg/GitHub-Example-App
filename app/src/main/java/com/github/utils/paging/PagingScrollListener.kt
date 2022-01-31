package com.github.utils.paging

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class PagingScrollListener(
    private val layoutManager: RecyclerView.LayoutManager, private val visibleThreshold: Int,
    private val onLoadMoreListener: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var lastVisibleItem = 0
    var enabled = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dx == 0 && dy == 0) {
            return
        }
        val totalItemCount = layoutManager.itemCount.toLong()
        if (layoutManager is LinearLayoutManager) {
            lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        }
        if (layoutManager is StaggeredGridLayoutManager) {
            val columns = layoutManager
                .findLastVisibleItemPositions(null)
            lastVisibleItem = columns[columns.size - 1]
        }
        if (totalItemCount <= lastVisibleItem + visibleThreshold) {
            dispatchLoadMore()
        }
    }

    private fun dispatchLoadMore() {
        if (enabled) {
            onLoadMoreListener()
        }
    }
}