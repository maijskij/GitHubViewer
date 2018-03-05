package com.denis.githubviewer.data.github

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JakeWhartonReposApi {

    //https://api.github.com/users/JakeWharton/repos?page=1&per_page=15

    @GET("users/google/repos")
    fun getRepos(@Query("page") page: Int,
                 @Query("per_page") perPage: Int): Call<List<GitHubRepo>>
}
