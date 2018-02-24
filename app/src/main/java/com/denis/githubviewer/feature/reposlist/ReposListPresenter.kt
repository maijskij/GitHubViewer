package com.denis.githubviewer.feature.reposlist

import com.denis.githubviewer.App
import com.denis.githubviewer.data.github.GitHubRepo
import com.denis.githubviewer.data.github.GithubApi
import com.denis.githubviewer.data.DbApi
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject

class ReposListPresenter(private val view: ReposListContract.View) :ReposListContract.UserActionsListener {


    companion object {
        private const val STARTING_PAGE = 1
    }

    @Inject
    lateinit var api: GithubApi

    @Inject
    lateinit var db: DbApi

    private var job: Job? = null

    // starting values
    private var isLoading:  Boolean = false
    private var isLastPage: Boolean = false
    private var pageToLoad: Int = STARTING_PAGE


    init{
        App.component.inject( this )
    }

    override fun loadData() {

        if (isLastPage || isLoading) return

        job = launch(UI) {

            isLoading = true
            view.setProgressIndicator(true)

            var newData: List<GitHubRepo>

            try {
                newData = getOnlineData(pageToLoad)

                pageToLoad += 1

                if (newData.isEmpty()) {
                    isLastPage = true

                } else {

                    if (isOnlyFirstPageLoaded()) {
                        // first time successful network request
                        // clean all offline data from DB
                        db.eraseOfflineData()
                        view.clearData()
                    }

                    // save new data in DB
                    db.saveData(newData)
                    view.addData(newData)
                }
            } catch (e: Throwable) {

                newData = db.getOfflineData()
                view.clearData()
                view.addData(newData)
                view.showSnackbar( e.message.orEmpty(), {loadData()} )
            }

            view.setProgressIndicator(false)
            isLoading = false
        }

    }

    override fun cancelLoad() {
        job?.cancel()
        job = null
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

    private fun isOnlyFirstPageLoaded(): Boolean = pageToLoad == STARTING_PAGE + 1
}