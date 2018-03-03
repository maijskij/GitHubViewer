package com.denis.githubviewer.feature.reposlist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denis.githubviewer.App
import com.denis.githubviewer.R
import com.denis.githubviewer.data.db.DbApi
import com.denis.githubviewer.feature.reposlist.adapter.GithubRepositoryAdapter
import com.denis.githubviewer.feature.reposlist.adapter.PaginatedScrollListener
import com.denis.githubviewer.data.github.GitHubRepo
import com.denis.githubviewer.data.github.GithubApi
import kotlinx.android.synthetic.main.fragment_repos.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject


class ReposListFragment: Fragment(), ReposListContract.View {

    companion object {
        private const val KEY_DATA_LIST    = "KEY_DATA_LIST"
    }

    private lateinit var reposAdapter: GithubRepositoryAdapter

    private lateinit var actionsListener: ReposListContract.UserActionsListener

    @Inject
    lateinit var dbApi: DbApi

    @Inject
    lateinit var gitHubApi: GithubApi

    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.reposListComponent.inject( this )
        reposAdapter = GithubRepositoryAdapter()
        actionsListener = ReposListPresenter(this, dbApi, gitHubApi)
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
            addOnScrollListener(PaginatedScrollListener({requestData()}, layout))

            adapter = reposAdapter
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_DATA_LIST)) {
            val list: List<GitHubRepo>  = savedInstanceState.getParcelableArrayList(KEY_DATA_LIST)
            reposAdapter.add(list)

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
            outState.putParcelableArrayList(KEY_DATA_LIST, ArrayList<GitHubRepo>(data))
        }
    }

    override fun clearData() {
        reposAdapter.clear()
    }

    override fun addData(data: List<GitHubRepo>) {
        reposAdapter.add(data)
    }

    override fun showSnackbar(msg: String) =
            Snackbar.make(list, msg , Snackbar.LENGTH_LONG)
                    .setAction("RETRY") { requestData() }
                    .show()

    override fun setProgressIndicator(active: Boolean) {
        reposAdapter.showLoadingItem = active
    }

    private fun requestData(){
        job = launch(UI) {
            actionsListener.loadData()
        }
    }

}