package com.denis.githubviewer.data

import com.denis.githubviewer.data.github.GitHubRepo

interface  DbApi {
    suspend fun eraseOfflineData()
    suspend fun saveData(data: List<GitHubRepo>)
    suspend fun getOfflineData(): List<GitHubRepo>
}