package com.denis.githubviewer


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.denis.githubviewer.feature.reposlist.ReposListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class TestViews {

    @Rule @JvmField
    val activityActivityTestRule = ActivityTestRule<ReposListActivity>(ReposListActivity::class.java)

    @Test
    fun testReposRecycleViewIsDisplayed() {

        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }
}