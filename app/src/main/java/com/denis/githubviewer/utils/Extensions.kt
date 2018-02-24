package com.denis.githubviewer.utils

import android.os.Bundle
import android.os.Parcelable



fun <T : Parcelable> Bundle.putParcelableList(key: String, value: List<T>) {
    this.putParcelableArrayList(key, kotlin.collections.ArrayList<T>(value))
}
