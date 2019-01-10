package com.github.leonardoz.kanbapp.view.fragment

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.github.leonardoz.kanbapp.BoardsActivity
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.BoardRestrictions
import com.github.leonardoz.kanbapp.util.BaseUiTest
import com.github.leonardoz.kanbapp.util.RecyclerViewMatcher
import com.github.leonardoz.kanbapp.util.format
import com.github.leonardoz.kanbapp.util.hasTextInputLayoutErrorText
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateBoardFragmentTest : BaseUiTest() {

    // Base Activity
    @get:Rule
    private val rule: ActivityTestRule<BoardsActivity> =
        ActivityTestRule(
            BoardsActivity::class.java,
            false,
            true
        )

    private var context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun prepareData() {
        rule.launchActivity(null)
        goToCreateBoard()
    }

    private fun goToCreateBoard() {
        onView(withId(R.id.createBoard))
            .perform(click())
    }

    @Test
    fun shouldBeIn_InitialState() {
        checkCreateBoardIsDisabled()

        onView(withId(R.id.etName))
            .check(matches(hasFocus()))
    }

    @Test
    fun shouldShow_RequiredError_And_MaxLengthError() {
        val expectedMaxLengthError = format(context.getString(R.string.max_length), BoardRestrictions.nameSizeLimit)
        val expectedRequiredError = context.getString(R.string.required)
        // Type invalid text
        onView(withId(R.id.etNameInput))
            .perform(typeTextIntoFocusedView("This will completely explode!"))

        checkCreateBoardIsDisabled()

        // Check if error appears
        onView(withId(R.id.etName))
            .check(matches(hasTextInputLayoutErrorText(expectedMaxLengthError)))

        // Clean input
        onView(withId(R.id.etNameInput))
            .perform(clearText())

        checkCreateBoardIsDisabled()

        // Check if error appear
        onView(withId(R.id.etName))
            .check(matches((hasTextInputLayoutErrorText(expectedRequiredError))))
    }

    @Test
    fun shouldCreate_NewBoard() {
        // Type name
        onView(withId(R.id.etNameInput))
            .perform(typeTextIntoFocusedView("Daily tasks"))

        // Button should be enabled and click can happen
        onView(withId(R.id.createBoard))
            .check((matches(isEnabled())))
            .perform(click())

        // Check if new item is in RecyclerView
        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.board_title)
        ).check(matches(withText("Daily tasks")))

    }

    private fun checkCreateBoardIsDisabled() {
        onView(withId(R.id.createBoard))
            .check(matches(withText(R.string.create_board)))
            .check((matches(not(isEnabled()))))
    }


}