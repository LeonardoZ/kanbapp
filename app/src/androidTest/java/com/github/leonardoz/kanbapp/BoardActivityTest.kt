package com.github.leonardoz.kanbapp


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BoardActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<BoardsActivity> = ActivityTestRule(BoardsActivity::class.java)

    @Test
    fun click_addButton() {
        onView(withId(R.id.main_label)).check(matches(withText("Boards activity")))
    }
}
