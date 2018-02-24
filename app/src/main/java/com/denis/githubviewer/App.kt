package com.denis.githubviewer

import android.app.Application
import com.denis.githubviewer.di.ApplicationComponent
import com.denis.githubviewer.di.DaggerApplicationComponent
import io.realm.Realm

class App : Application() {

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);

        // Dagger2
        component = DaggerApplicationComponent.builder().build()
    }
}