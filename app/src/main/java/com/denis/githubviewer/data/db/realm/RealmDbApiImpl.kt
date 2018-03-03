package com.denis.githubviewer.data.db.realm

import com.denis.githubviewer.data.db.DbApi
import com.denis.githubviewer.data.github.GitHubRepo
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.coroutines.experimental.async

class RealmDbApiImpl : DbApi {

    override suspend fun eraseOfflineData() =
            async {
                // Realm implements the Closable interface,
                // therefore we can make use of Kotlin's built-in
                // extension method 'use'.
                Realm.getDefaultInstance().use {
                    it.executeTransaction {
                        it.deleteAll()
                    }
                }

            }.await()

    override suspend fun saveData(data: List<GitHubRepo>) = async {

        Realm.getDefaultInstance().use {
            it.executeTransaction {
                for (item in data) {
                    val repo = it.createObject<RealmRepoItem>()
                    repo.name = item.name
                }
            }
        }
    }.await()


    override suspend fun getOfflineData(): List<GitHubRepo>  {

        val offlineData = ArrayList<GitHubRepo>()
        async {

            Realm.getDefaultInstance().use {
                offlineData.addAll(
                        it
                                .where<RealmRepoItem>()
                                .findAll()
                                .map { GitHubRepo(it.name ?: "") }
                )
            }


        }.await()

        return offlineData
    }

}