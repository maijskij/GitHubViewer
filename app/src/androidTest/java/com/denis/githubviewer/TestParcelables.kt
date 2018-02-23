package com.denis.githubviewer

import android.os.Parcel
import android.support.test.runner.AndroidJUnit4
import com.denis.githubviewer.data.github.GitHubRepo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestParcelables {

    companion object {
        val TEST_NAME = "This is a string"
    }


    @Test
    fun gitHubRepo_ParcelableWriteRead() {

        // Set up object's data
        val repo = GitHubRepo(TEST_NAME)

        // Write the data.
        val parcel = Parcel.obtain()
        repo.writeToParcel(parcel, repo.describeContents())

        // After you're done with writing, you need to reset the parcel for reading.
        parcel.setDataPosition(0)

        // Read the data.
        val createdFromParcel = GitHubRepo.CREATOR.createFromParcel(parcel)

        assertEquals(createdFromParcel.name, TEST_NAME)
    }
}
