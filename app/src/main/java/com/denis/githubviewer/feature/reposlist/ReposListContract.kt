package com.denis.githubviewer.feature.reposlist

import com.denis.githubviewer.data.github.GitHubRepo

interface ReposListContract {

    interface View {

        fun setProgressIndicator(active: Boolean)

        fun addData(data: List<GitHubRepo>)

        fun clearData()

        fun showSnackbar(msg: String, retry: () -> Unit)
    }

    interface UserActionsListener {

        fun loadData()
        fun cancelLoad()
    }
}
