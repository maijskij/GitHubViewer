package com.denis.githubviewer.feature.reposlist.adapter


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class PaginatedScrollListener(private val loadMoreItems: () -> Unit, var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    companion object {
        private const val VISIBLE_THRESHOLD = 3
    }

    private var firstVisibleItemPosition = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)


        if (dy > 0) {

            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if ( thresholdExceeded() && firstVisibleItemPosition >= 0 ) {
                loadMoreItems()
            }
        }
    }

    private fun thresholdExceeded() : Boolean =
            (visibleItemCount + firstVisibleItemPosition) >= (totalItemCount - VISIBLE_THRESHOLD)

}