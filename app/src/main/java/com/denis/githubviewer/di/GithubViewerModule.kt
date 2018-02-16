package com.denis.githubviewer.di

import com.denis.githubviewer.api.GithubApi
import com.denis.githubviewer.api.GithubRestApi
import com.denis.githubviewer.api.JakeWhartonReposApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class GithubViewerModule {

    @Provides
    @Singleton
    fun provideGithubAPI(githubApi: JakeWhartonReposApi): GithubApi = GithubRestApi(githubApi)

    @Provides
    @Singleton
    fun provideJakeWhartonsApi(retrofit: Retrofit): JakeWhartonReposApi = retrofit.create(JakeWhartonReposApi::class.java)

}