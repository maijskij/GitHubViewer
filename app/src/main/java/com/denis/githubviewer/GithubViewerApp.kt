package com.denis.githubviewer

import android.app.Application
import com.denis.githubviewer.di.DaggerGithubViewerComponent
import com.denis.githubviewer.di.GithubViewerComponent
import io.realm.Realm

class GithubViewerApp : Application() {

    companion object {
        lateinit var reposComponent: GithubViewerComponent
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);

        reposComponent = DaggerGithubViewerComponent.builder()
                .build()
    }
}