package com.denis.githubviewer.feature.reposlist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.denis.githubviewer.R

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        // Nothing to bind
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            LoadingViewHolder(parent)


    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))

}