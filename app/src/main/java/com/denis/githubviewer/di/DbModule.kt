package com.denis.githubviewer.di

import com.denis.githubviewer.data.DbApi
import com.denis.githubviewer.data.realm.RealmStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
    @Provides
    @Singleton
    fun provideDbApi(): DbApi = RealmStorage()
}
