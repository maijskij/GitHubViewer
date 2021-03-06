// allow "natural" test names
@file:Suppress("IllegalIdentifier")

package com.denis.githubviewer.feature.reposlist

import com.denis.githubviewer.testutils.MockedCall
import com.denis.githubviewer.data.db.DbApi
import com.denis.githubviewer.data.github.GitHubRepo
import com.denis.githubviewer.data.github.GithubApi

import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.experimental.*

import org.junit.Before
import org.junit.Test
import org.mockito.*


class ReposListPresenterTest {

    companion object {

        private val DATA_LIST = listOf(GitHubRepo("Repo1"), GitHubRepo("Repo2"))
        private val EMPTY_DATA_LIST: List<GitHubRepo> = emptyList()
    }

    @Mock
    private lateinit var gitHubApi: GithubApi

    @Mock
    private lateinit var dbApi: DbApi

    @Mock
    private lateinit var view: ReposListContract.View

    // Class to test
    private lateinit var presenter: ReposListPresenter

    // Class to test
    private lateinit var customPresenter: ReposListPresenter


    @Before
    fun setupNotesPresenter() {
        // dbApi = Mockito.mock(DbApi::class.java, withSettings().verboseLogging())

        // Initializes objects annotated with Mockito annotations
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        presenter = ReposListPresenter(view, dbApi, gitHubApi)

        // Presenter with manually mocked dbApi
        customPresenter = ReposListPresenter(view, DbApiImpl(), gitHubApi)

    }


    @Test
    fun `Progress view is shown during the data loading and hidden when finished`() = testBlocking {

        // GitHubApi is mocked to return some data
        val callMock = MockedCall(DATA_LIST)
        whenever(gitHubApi.getData(any(), any())).thenReturn(callMock)

        // When loading of GitHub repository list is requested
        presenter.loadData()

        // In proper oreder
        val progressOrder = inOrder(view)
        // Progress indicator is shown in UI
        progressOrder.verify(view, times(1)).setProgressIndicator(true)
        // And then progress indicator is hidden from UI in
        progressOrder.verify(view, times(1)).setProgressIndicator(false)
    }


    @Test
    fun `Proper data from GitHub is passed to a view`() = testBlocking {

        // GitHubApi is mocked to return DATA_LIST
        val callMock = MockedCall(DATA_LIST)
        whenever(gitHubApi.getData(any(), any())).thenReturn(callMock)

        // When loading of GitHub repository list is requested
        presenter.loadData()

        // Then proper DATA_LIST is shown in UI
        verify(view, times(1)).addData(DATA_LIST)
    }


    @Test
    fun `Data is saved in DB after successful request to GitHub`() = testBlocking {

        // GitHubApi is mocked to return DATA_LIST
        val callMock = MockedCall(DATA_LIST)
        whenever(gitHubApi.getData(any(), any())).thenReturn(callMock)

        // When loading of GitHub repository list is requested
        presenter.loadData()

        // DATA_LIST is saved to database
        verify(dbApi, times(1)).saveData(DATA_LIST)  //TODO: Second parameter mismatch
    }


    @Test
    fun `Presenter enters Last page state when GitHib return empty list`() = testBlocking {

        // GitHubApi is mocked to return EMPTY_DATA_LIST
        val callMock = MockedCall(EMPTY_DATA_LIST)
        whenever(gitHubApi.getData(any(), any())).thenReturn(callMock)

        // When loading of GitHub repository list is requested
        presenter.loadData()

        // saveData is not called
        verify(dbApi, never()).saveData(EMPTY_DATA_LIST)  //TODO: Any()
    }



    @Test
    fun `Proper data from DB is passed to a view, when GitHibApi throws an Exception`() = testBlocking {

        // GitHubApi is mocked to thrown an Exception
        val callMock = MockedCall<List<GitHubRepo>>(exception = Throwable())
        whenever(gitHubApi.getData(com.nhaarman.mockito_kotlin.any(), com.nhaarman.mockito_kotlin.any())).thenReturn(callMock)

        // View is mocked to return that it doesn't contain any data in a list yet
        whenever(view.hasNoData()).thenReturn(true)

        // When data is requested
        customPresenter.loadData()

        // Then proper DATA_LIST is shown in UI
        verify(view, times(1)).addData(DATA_LIST)
        // Then exception message is shown in UI
        verify(view, times(1)).showSnackbar(any())
    }

    @Test
    fun `Data from DB is NOT passed to a view, when GitHibApi throws an Exception, but view ALREADY have data`() = testBlocking() {

        // GitHubApi is mocked to thrown an Exception
        val callMock = MockedCall<List<GitHubRepo>>(exception = Throwable())
        whenever(gitHubApi.getData(any(), any())).thenReturn(callMock)

        // View is mocked to return that it already contains data in the list
        whenever(view.hasNoData()).thenReturn(false)

        // When loading of GitHub repository list is requested
        customPresenter.loadData()

        // Then proper DATA_LIST is shown in UI
        verify(view, never()).addData(any())

        // Then exception message is shown in UI
        verify(view, times(1)).showSnackbar(any())
    }


    // Due to suspend functions "whenever" mocking always return null.
    // Implementing mocked class manually
    private class DbApiImpl : DbApi {
        override suspend fun saveData(data: List<GitHubRepo>) {
            // nothing
        }

        override suspend fun getOfflineData(): List<GitHubRepo> {
            return DATA_LIST
        }

        override suspend fun eraseOfflineData() {
            // nothing
        }
    }

//    private fun testBlocking(block: suspend () -> Unit) {
//        runBlocking { block() }
//    }

    private fun testBlocking(block: suspend CoroutineScope.() -> Unit) {
        runBlocking(Unconfined, block)
    }
}