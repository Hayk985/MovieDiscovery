package com.moviediscovery.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewEndReachedListener(
    private val rvLayoutManager: RecyclerView.LayoutManager?,
) : RecyclerView.OnScrollListener() {

    abstract fun onEndReached()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        rvLayoutManager?.let {
            if (it is GridLayoutManager) {
                if (isEndReached(recyclerView, it, dy)) {
                    onEndReached()
                }
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun isEndReached(
        recyclerView: RecyclerView,
        rvLayoutManager: GridLayoutManager,
        dy: Int,
    ): Boolean {
        return dy > 0 &&
            recyclerView.adapter?.itemCount!! > 0 &&
            rvLayoutManager.findLastVisibleItemPosition() == rvLayoutManager.itemCount - 1 &&
            !recyclerView.canScrollVertically(1)
    }
}