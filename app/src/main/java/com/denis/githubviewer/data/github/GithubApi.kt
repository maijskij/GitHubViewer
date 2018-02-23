package com.denis.githubviewer.data.github

import retrofit2.Call

interface GithubApi {

    fun getData(page: Int, perPage: Int): Call<List<GitHubRepo>>
}

