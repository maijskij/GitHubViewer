package com.denis.githubviewer.api

import com.denis.githubviewer.github.GitHubRepo
import retrofit2.Call

interface GithubApi {

    fun getData(page: Int, perPage: Int): Call<List<GitHubRepo>>
}

