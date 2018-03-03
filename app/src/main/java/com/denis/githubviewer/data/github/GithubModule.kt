package com.denis.githubviewer.data.github

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class GithubModule {

    @Provides
    @Singleton
    fun provideGithubAPI(githubApi: JakeWhartonReposApi): GithubApi = GithubRestApi(githubApi)

    @Provides
    @Singleton
    fun provideJakeWhartonsApi(retrofit: Retrofit): JakeWhartonReposApi = retrofit.create(JakeWhartonReposApi::class.java)

}