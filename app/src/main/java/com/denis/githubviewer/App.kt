package com.denis.githubviewer

import android.app.Application
import com.denis.githubviewer.feature.reposlist.DaggerReposListComponent
import com.denis.githubviewer.feature.reposlist.ReposListComponent
import io.realm.Realm

class App : Application() {

    companion object {
        lateinit var reposListComponent: ReposListComponent
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);

        // Dagger2
        reposListComponent = DaggerReposListComponent.builder().build()
    }
}