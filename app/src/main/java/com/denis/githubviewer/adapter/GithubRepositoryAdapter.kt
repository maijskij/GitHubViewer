package com.denis.githubviewer.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.denis.githubviewer.github.GitHubRepo

class GithubRepositoryAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var showLoadingItem: Boolean = true
        set(value){
            if (showLoadingItem != value)
                if (value){
                    addLoadingItem()
                }else {
                    removeLoadingItem()
                }
        }

    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem =  object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {

        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.REPOS, ReposDelegateAdapter())
        items = java.util.ArrayList()
        items.add(loadingItem)
    }

    override fun getItemCount(): Int =
            items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            delegateAdapters.get(getItemViewType(position)).onBindViewHolder( holder, items[position])


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun getItemViewType(position: Int): Int =
            items[position].getViewType()


    fun add(repos: List<GitHubRepo>) {

        if (repos.isEmpty()) return

        removeLoadingItem()

        val initPosition = getLastPosition()
        items.addAll(repos)
        notifyItemRangeChanged( initPosition, repos.size )

        if ( showLoadingItem ) addLoadingItem()
    }

    fun getData(): List<GitHubRepo> =
            items
                    .filter { it.getViewType() == AdapterConstants.REPOS }
                    .map { it as GitHubRepo }


    fun clear() {
        if (items.isEmpty()) return

        val position = getLastPosition()
        items.clear()
        notifyItemRangeRemoved(0, position + 1)
    }


    private fun getLastPosition(): Int =
            if ( items.lastIndex < 0) 0 else items.lastIndex


    private fun removeLoadingItem(){
        if (items.isEmpty()) return

        val position = items.size - 1
        if (items[position].getViewType() == AdapterConstants.LOADING) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun addLoadingItem(){

        val position = items.size - 1
        if ( position < 0 || items[position].getViewType() != AdapterConstants.LOADING) {
            items.add(loadingItem)
            notifyItemInserted( items.size -1 )
        }
    }
}





