package com.denis.githubviewer.api

import com.denis.githubviewer.github.GitHubRepo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JakeWhartonReposApi {

    //https://api.github.com/users/JakeWharton/repos?page=1&per_page=15

    @GET("users/JakeWharton/repos")
    fun getRepos(@Query("page") page: Int,
                 @Query("per_page") perPage: Int): Call<List<GitHubRepo>>
}
