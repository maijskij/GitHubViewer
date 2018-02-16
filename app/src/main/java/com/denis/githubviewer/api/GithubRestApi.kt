package com.denis.githubviewer.api

import com.denis.githubviewer.github.GitHubRepo
import retrofit2.Call
import javax.inject.Inject

class GithubRestApi @Inject constructor(private val githubApi: JakeWhartonReposApi) : GithubApi {

    override fun getData(page: Int, perPage: Int): Call<List<GitHubRepo>> {
        return githubApi.getRepos(page, perPage)
    }
}

