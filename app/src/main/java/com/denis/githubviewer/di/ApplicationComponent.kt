package com.denis.githubviewer.di

import com.denis.githubviewer.feature.reposlist.ReposListFragment
import com.denis.githubviewer.feature.reposlist.ReposListPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        GithubViewerModule::class,
        NetworkModule::class,
        DbModule::class)
)
interface ApplicationComponent {
    fun inject(reposListPresenter: ReposListPresenter)
}