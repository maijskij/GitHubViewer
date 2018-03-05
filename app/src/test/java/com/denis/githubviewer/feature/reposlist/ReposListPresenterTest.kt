// allow "natural" test names
@file:Suppress("IllegalIdentifier")
package com.denis.githubviewer.feature.reposlist

import com.denis.githubviewer.MockedCall
import com.denis.githubviewer.data.db.DbApi
import com.denis.githubviewer.data.github.GitHubRepo
import com.denis.githubviewer.data.github.GithubApi
import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.`when`

class ReposListPresenterTest{

    companion object {

        private val DATA_LIST = listOf(GitHubRepo("Repo1"), GitHubRepo("Repo2"))
        private val EMPTY_DATA_LIST:List<GitHubRepo> = emptyList()
    }


    @Mock
    private lateinit var gitHubApi: GithubApi

    @Mock
    private lateinit var dbApi: DbApi

    @Mock
    private lateinit var view: ReposListContract.View

    // Object to test
    private lateinit var presenter: ReposListPresenter


    @Before
    fun setupNotesPresenter() {
        // dbApi = Mockito.mock(DbApi::class.java, withSettings().verboseLogging())

        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        presenter = ReposListPresenter(view, dbApi, gitHubApi)
    }

    @Test
    fun `Check, that proper data from GitHub is passed to a view`() {
        // When loading of data is requested
        runBlocking {

            // GitHubApi is mocked to return DATA_LIST
            val callMock = MockedCall(DATA_LIST)
            whenever(gitHubApi.getData(com.nhaarman.mockito_kotlin.any(), com.nhaarman.mockito_kotlin.any())).thenReturn(callMock)

           // DATA_LIST.mockApiCall(gitHubApi)
            // When loading of GitHub repository list is requested
            presenter.loadData()

            // Then progress indicator is shown and then hidden in proper order
            val progressOrder = inOrder(view)
            // Progress indicator is shown in UI
            progressOrder.verify(view, times(1)).setProgressIndicator(true)
            // Then progress indicator is hidden from UI
            progressOrder.verify(view, times(1)).setProgressIndicator(false)

            // Then proper DATA_LIST is shown in UI
            verify(view).addData(DATA_LIST)
        }
    }

    @Test
    fun `Check, that proper data from DB is passed to a view, when GitHibApi throws an Exception`() {
        // When loading of data is requested
        runBlocking {

            // Using launch, to wait for suspend function dbApi.getOfflineData()
            launch {

                // GitHubApi is mocked to thrown an Exception
                val callMock = MockedCall<List<GitHubRepo>>(exception = Throwable())
                whenever(gitHubApi.getData(com.nhaarman.mockito_kotlin.any(), com.nhaarman.mockito_kotlin.any())).thenReturn(callMock)

                `when`(dbApi.getOfflineData()).thenReturn(DATA_LIST)

                // View is mocked to return that it doesn't contain any data in a list yet
                whenever(view.hasNoData()).thenReturn(true )



                presenter.loadData()


                // Then progress indicator is shown and then hidden in proper order
                val progressOrder = inOrder(view)
                // Progress indicator is shown in UI
                progressOrder.verify(view, times(1)).setProgressIndicator(true)
                // Then progress indicator is hidden from UI
                progressOrder.verify(view, times(1)).setProgressIndicator(false)

                // Then proper DATA_LIST is shown in UI
                verify(view).addData(DATA_LIST)
                // Then exception message is shown in UI
                verify(view).showSnackbar(any())
            }
        }
    }

    @Test
    fun `Check, that data from DB is NOT passed to a view, when GitHibApi throws an Exception, but view ALREADY have data`() {
        // When loading of data is requested
        runBlocking {

            // Using launch, to wait for suspend function dbApi.getOfflineData()
            launch {

                // GitHubApi is mocked to thrown an Exception
                val callMock = MockedCall<List<GitHubRepo>>(exception = Throwable())
                whenever(gitHubApi.getData(com.nhaarman.mockito_kotlin.any(), com.nhaarman.mockito_kotlin.any())).thenReturn(callMock)

                // DbApi is mocked to return DATA_LIST
                whenever(dbApi.getOfflineData()).thenReturn(DATA_LIST)

                // View is mocked to return that it already contains data in the list
                whenever(view.hasNoData()).thenReturn(false)


                // When loading of GitHub repository list is requested
                presenter.loadData()

                // Then progress indicator is shown and then hidden in proper order
                val progressOrder = inOrder(view)
                // Progress indicator is shown in UI
                progressOrder.verify(view, times(1)).setProgressIndicator(true)
                // Then progress indicator is hidden from UI
                progressOrder.verify(view, times(1)).setProgressIndicator(false)

                // Then proper DATA_LIST is shown in UI
                verify(view, never()).addData(DATA_LIST)
                // Then exception message is shown in UI
                verify(view).showSnackbar(any())
            }
        }
    }
}