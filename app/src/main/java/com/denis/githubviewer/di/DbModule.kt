package com.denis.githubviewer.di

import com.denis.githubviewer.api.DbApi
import com.denis.githubviewer.realm.RealmStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
    @Provides
    @Singleton
    fun provideDbApi(): DbApi = RealmStorage()
}
