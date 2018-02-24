package com.denis.githubviewer.feature.reposlist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denis.githubviewer.R
import com.denis.githubviewer.feature.reposlist.adapter.GithubRepositoryAdapter
import com.denis.githubviewer.feature.reposlist.adapter.PaginatedScrollListener
import com.denis.githubviewer.data.github.GitHubRepo
import com.denis.githubviewer.utils.putParcelableList
import kotlinx.android.synthetic.main.fragment_repos.*



class ReposListFragment: Fragment(), ReposListContract.View {

    companion object {
        private const val KEY_DATA_LIST    = "KEY_DATA_LIST"
    }

    private lateinit var reposAdapter: GithubRepositoryAdapter

    private lateinit var actionsListener: ReposListContract.UserActionsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reposAdapter = GithubRepositoryAdapter()
        actionsListener = ReposListPresenter(this)
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
            addOnScrollListener(PaginatedScrollListener({actionsListener.loadData()}, layout))

            adapter = reposAdapter
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_DATA_LIST)) {

            val list   = savedInstanceState.get(KEY_DATA_LIST) as ArrayList<*>
            val dataList: List<GitHubRepo>  = list.filterIsInstance<GitHubRepo>()
            reposAdapter.add(dataList)

        } else {
            actionsListener.loadData()
        }
    }

    override fun onPause() {
        super.onPause()

        actionsListener.cancelLoad()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val data = reposAdapter.getData()
        if ( data.isNotEmpty() ) {
            outState.putParcelableList(KEY_DATA_LIST, data)
        }
    }

    override fun clearData() {
        reposAdapter.clear()
    }

    override fun addData(data: List<GitHubRepo>) {
        reposAdapter.add(data)
    }

    override fun showSnackbar(msg: String, retry: () -> Unit) {
        Snackbar.make(list, msg , Snackbar.LENGTH_LONG)
                .setAction("RETRY") { retry }
                .show()
    }

    override fun setProgressIndicator(active: Boolean) {
        reposAdapter.showLoadingItem = active
    }
}