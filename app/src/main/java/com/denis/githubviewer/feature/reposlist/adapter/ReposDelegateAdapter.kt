package com.denis.githubviewer.feature.reposlist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.denis.githubviewer.R
import com.denis.githubviewer.data.github.GitHubRepo
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposDelegateAdapter : ViewTypeDelegateAdapter {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as GitHubRepoViewHolder
        holder.bind(item as GitHubRepo)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            GitHubRepoViewHolder(parent)


    inner class GitHubRepoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)){


        private val name =  itemView.name

        fun bind(item: GitHubRepo){
            name.text = item.name
        }
    }

}

