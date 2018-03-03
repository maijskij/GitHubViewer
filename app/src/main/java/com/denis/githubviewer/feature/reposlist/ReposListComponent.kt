package com.denis.githubviewer.feature.reposlist

import com.denis.githubviewer.data.db.realm.DbModule
import com.denis.githubviewer.data.github.GithubModule
import com.denis.githubviewer.data.github.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(GithubModule::class), (NetworkModule::class), (DbModule::class)])

interface ReposListComponent {
    fun inject(reposListFragment: ReposListFragment)
}