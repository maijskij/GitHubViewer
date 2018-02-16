package com.denis.githubviewer.api

import com.denis.githubviewer.github.GitHubRepo

interface  DbApi {
    suspend fun eraseOfflineData()
    suspend fun saveData(data: List<GitHubRepo>)
    suspend fun getOfflineData(): List<GitHubRepo>
}