package com.denis.githubviewer.data.db.realm

import com.denis.githubviewer.data.db.DbApi
import com.denis.githubviewer.data.db.realm.RealmDbApiImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
    @Provides
    @Singleton
    fun provideDbApi(): DbApi = RealmDbApiImpl()
}
