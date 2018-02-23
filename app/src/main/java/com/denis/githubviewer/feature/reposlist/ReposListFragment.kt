package com.denis.githubviewer.feature.reposlist

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denis.githubviewer.R
import com.denis.githubviewer.feature.reposlist.adapter.GithubRepositoryAdapter
import com.denis.githubviewer.feature.reposlist.adapter.PaginatedScrollListener
import com.denis.githubviewer.app.GithubViewerApp
import com.denis.githubviewer.data.github.GitHubRepo
import kotlinx.android.synthetic.main.fragment_repos.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject


class ReposListFragment: Fragment() {

    companion object {
        private const val KEY_DATA_LIST    = "KEY_DATA_LIST"
        private const val KEY_PAGE_TO_LOAD = "KEY_PAGE_TO_LOAD"
        private const val KEY_IS_LAST_PAGE = "KEY_IS_LAST_PAGE"
    }

    @Inject
    lateinit var reposManager: GithubManager
    private lateinit var reposAdapter: GithubRepositoryAdapter

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GithubViewerApp.reposComponent.inject(this)
        reposAdapter = GithubRepositoryAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list.apply {
            setHasFixedSize( true )

            val layout = LinearLayoutManager(context)
            layoutManager = layout

            clearOnScrollListeners()
            addOnScrollListener(ScrollListener({requestData()}, layout))

            adapter = reposAdapter
        }

        if (savedInstanceState != null
                && savedInstanceState.containsKey(KEY_PAGE_TO_LOAD)
                && savedInstanceState.containsKey(KEY_DATA_LIST)
                && savedInstanceState.containsKey(KEY_IS_LAST_PAGE)) {

            reposManager.pageToLoad = savedInstanceState.getInt(KEY_PAGE_TO_LOAD)
            reposManager.isLastPage = savedInstanceState.getBoolean(KEY_IS_LAST_PAGE)
            val list   = savedInstanceState.get(KEY_DATA_LIST) as ArrayList<*>
            val dataList: List<GitHubRepo>  = list.filterIsInstance<GitHubRepo>()


            reposAdapter.apply {
                add(dataList)

                if (reposManager.isLastPage) {
                    showLoadingItem = false
                }

            }
        } else {
            requestData()
        }
    }

    override fun onPause() {
        super.onPause()

        job?.cancel()
        job = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val data = reposAdapter.getData()
        if ( data.isNotEmpty() ) {
            outState.putParcelableList(KEY_DATA_LIST, data)
            outState.putBoolean(KEY_IS_LAST_PAGE, reposManager.isLastPage)
            outState.putInt(KEY_PAGE_TO_LOAD, reposManager.pageToLoad)
        }
    }


    private fun requestData() {

        if (reposManager.isLastPage) return

        job = launch(UI) {

            reposAdapter.showLoadingItem = true
            val newData = reposManager.getData()
            reposAdapter.showLoadingItem = false

            if (reposManager.isOnline) {

                if (reposManager.isOnlyFirstPageLoaded()) {
                    // first time successful network request
                    // remove offline data from adapter, if any
                    reposAdapter.clear()
                }

                reposAdapter.add(newData)

            } else { // offline
                if (reposAdapter.getData().isEmpty()) {
                    // show offline data, only if nothing from online data is yet loaded
                    reposAdapter.add(newData)
                }

                if (isVisible) {
                    Snackbar.make(list, getString(R.string.data_load_exception_error), Snackbar.LENGTH_LONG)
                            .setAction("RETRY") { requestData() }
                            .show()
                }
            }
        }
    }

    inner class ScrollListener(loadMoreItems: () -> Unit, layoutManager: LinearLayoutManager) : PaginatedScrollListener(loadMoreItems, layoutManager) {
        override fun isLoading():  Boolean  = reposManager.isLoading
        override fun isLastPage(): Boolean = reposManager.isLastPage
    }

}

// Bundle Extension for local use
private fun <T :Parcelable> Bundle.putParcelableList(key: String, value: List<T>) {
    this.putParcelableArrayList(key, kotlin.collections.ArrayList<T>(value))
}


