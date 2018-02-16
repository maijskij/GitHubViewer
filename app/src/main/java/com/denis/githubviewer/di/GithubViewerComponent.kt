package com.denis.githubviewer.di

import com.denis.githubviewer.ReposListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        GithubViewerModule::class,
        NetworkModule::class,
        DbModule::class)
)
interface GithubViewerComponent {
    fun inject(reposFragment: ReposListFragment)
}