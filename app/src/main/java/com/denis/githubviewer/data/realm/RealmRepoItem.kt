package com.denis.githubviewer.data.realm

import io.realm.RealmObject

open class RealmRepoItem : RealmObject() {
    var name: String? = null
}