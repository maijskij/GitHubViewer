package com.denis.githubviewer.feature.reposlist

import com.denis.githubviewer.data.realm.DbApi
import com.denis.githubviewer.data.github.GithubApi
import com.denis.githubviewer.data.github.GitHubRepo
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

class GithubManager @Inject constructor(private val api: GithubApi, private val db: DbApi) {

    companion object {
        private const val STARTING_PAGE = 1
    }

    // starting values
    var isOnline:   Boolean = true
    var isLoading:  Boolean = false
    var isLastPage: Boolean = false
    var pageToLoad: Int = STARTING_PAGE

    fun isOnlyFirstPageLoaded(): Boolean = pageToLoad == STARTING_PAGE + 1

    suspend fun getData(): List<GitHubRepo> {

        var list: List<GitHubRepo>

        isLoading = true
        try {
            list = getOnlineData(pageToLoad)
            isOnline = true
            pageToLoad += 1

            if (list.isEmpty()) {
                isLastPage = true

            } else {

                if ( isOnlyFirstPageLoaded() ) {
                    // first time successful network request
                    // clean all offline data from DB
                    db.eraseOfflineData()
                }

                // save new data in DB
                db.saveData(list)
            }
        } catch (e: Throwable) {

            list =  db.getOfflineData()
            isOnline = false
        }

        isLoading = false

        return list
    }

    private suspend fun getOnlineData(page: Int, perPage: Int = 15): List<GitHubRepo> {
        val result = api.getData(page, perPage).awaitResult()

        return when (result) {

            is ru.gildor.coroutines.retrofit.Result.Ok -> result.value
            is ru.gildor.coroutines.retrofit.Result.Error -> throw Throwable("HTTP error: ${result.response.message()}")
            is ru.gildor.coroutines.retrofit.Result.Exception -> throw result.exception
            else -> {
                throw Throwable("Something went wrong, please try again later.")
            }
        }
    }

}



