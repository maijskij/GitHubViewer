package com.denis.githubviewer


import com.denis.githubviewer.data.DbApi
import com.denis.githubviewer.data.github.GithubApi
import com.denis.githubviewer.data.github.GitHubRepo
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import org.mockito.Mockito


class GithubManagerTests {

    private val apiMock =  Mockito.mock(GithubApi::class.java)
    private val mockedDbApi = Mockito.mock(DbApi::class.java)

    private val emptyList:List<GitHubRepo> = emptyList()

    private val repoName1 = "1"
    private val repoName2 = "2"
    private val dataList = listOf(GitHubRepo(repoName1), GitHubRepo(repoName2))

    @Test
    fun success_checkResponseIsNotNull()   {

        runBlocking {
            // prepare
            emptyList.mockApiCall(apiMock)

            // call
            val newData = GithubManager(apiMock, mockedDbApi).getData()

            // assert
            assertNotNull(newData)
        }
    }

    @Test
    fun success_checkProperDataReceived()   {

        runBlocking {

            // prepare
            dataList.mockApiCall(apiMock)

            //call
            val newData = GithubManager(apiMock, mockedDbApi).getData()

            // assert
            assertNotNull(newData)
            assertEquals(newData.size, dataList.size)
            for (indx in 0 until newData.size){
                assertEquals(dataList.get(indx).name, newData.get(indx).name)
            }

        }
    }

    @Test
    fun failed_checkExceptionInResponse()   {

        runBlocking {
            //prepare
            val callMock = MockedCall<List<GitHubRepo>>(exception = Throwable())
            whenever(apiMock.getData(any(), any())).thenReturn(callMock)

            try {
                // call
                GithubManager(apiMock, mockedDbApi).getData()
                assert(false)
            }catch (e: Throwable){
                assert(true)
            }
        }
    }

    private fun List<GitHubRepo>.mockApiCall(apiMock: GithubApi) {
        val callMock = MockedCall<List<GitHubRepo>>(this)
        com.nhaarman.mockito_kotlin.whenever(apiMock.getData(com.nhaarman.mockito_kotlin.any(), com.nhaarman.mockito_kotlin.any())).thenReturn(callMock)
    }
}





