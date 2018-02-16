package com.denis.githubviewer.realm

import io.realm.RealmObject

open class RealmRepoItem : RealmObject() {
    var name: String? = null
}