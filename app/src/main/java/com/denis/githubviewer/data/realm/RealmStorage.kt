package com.denis.githubviewer.data.realm

import com.denis.githubviewer.data.github.GitHubRepo
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.coroutines.experimental.async

class RealmStorage : DbApi {

    override suspend fun eraseOfflineData() =
            async {
                val realm = Realm.getDefaultInstance()
                try {
                    realm.executeTransaction {
                        realm.deleteAll()
                    }
                } finally {
                    realm.close()
                }

            }.await()

    override suspend fun saveData(data: List<GitHubRepo>) = async {
        val realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                for (item in data) {
                    val repo = realm.createObject<RealmRepoItem>()
                    repo.name = item.name
                }
            }
        } finally {
            realm.close()
        }
    }.await()


    override suspend fun getOfflineData(): List<GitHubRepo>  {

        val offlineData = ArrayList<GitHubRepo>()
        async {

            // Realm implements the Closable interface,
            // therefore we can make use of Kotlin's built-in
            // extension method 'use'.
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