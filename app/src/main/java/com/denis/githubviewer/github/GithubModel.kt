package com.denis.githubviewer.github

import android.os.Parcel
import android.os.Parcelable
import com.denis.githubviewer.adapter.AdapterConstants
import com.denis.githubviewer.adapter.ViewType

data class GitHubRepo(
        val name: String
) : ViewType, Parcelable {

    // ViewType
    override fun getViewType(): Int =
            AdapterConstants.REPOS

    // Parcelable
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int)  =
        parcel.writeString(name)

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<GitHubRepo> {
        override fun createFromParcel(parcel: Parcel): GitHubRepo = GitHubRepo(parcel)
        override fun newArray(size: Int): Array<GitHubRepo?> = arrayOfNulls(size)
    }

}