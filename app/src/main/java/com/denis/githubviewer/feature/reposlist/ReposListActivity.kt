package com.denis.githubviewer.feature.reposlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.denis.githubviewer.R

class ReposListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.fragment_container, ReposListFragment())
            ft.commit()
        }
    }
}
