package com.github.leonardoz.kanbapp.view.dialog

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
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.dao.ColumnsDao
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.ColumnRestrictions
import com.github.leonardoz.kanbapp.util.BaseUiTest
import com.github.leonardoz.kanbapp.util.RecyclerViewMatcher
import com.github.leonardoz.kanbapp.util.format
import com.github.leonardoz.kanbapp.util.hasTextInputLayoutErrorText
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.allOf
import org.junit.*
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateColumnDialogTest : BaseUiTest() {

    // Base Activity
    @get:Rule
    private val rule: ActivityTestRule<BoardsActivity> =
        ActivityTestRule(
            BoardsActivity::class.java,
            false,
            true
        )
    private var context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var boardDao: BoardsDao
    private lateinit var columnDao: ColumnsDao

    private val board = Board(name = "Weekly Goals")

    init {
        val kanbapp: KanbappApplication? =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as? KanbappApplication
        requireNotNull(kanbapp)

        // prepare data
        boardDao = kanbapp.appComponent.exposeBoardsDao()
        columnDao = kanbapp.appComponent.exposeColumnsDao()
    }

    @Before
    fun prepareData() {
        board.id = boardDao.insertBoard(board)

        // launch activity and open the board
        rule.launchActivity(null)

        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.btnOpenBoard)
        ).perform(click())
    }

    @After
    fun cleanData() {
        boardDao.deleteBoard(board)
    }

    @Test
    fun shouldOpenDialog_AndTestForThe_Errors() {
        openDialogAndCheckIfItsOpen()

        val expectedMaxLengthError = format(
            context.getString(R.string.max_length),
            ColumnRestrictions.titleSizeLimit
        )
        val expectedRequiredError = context.getString(R.string.required)

        // Type invalid text
        onView(withId(R.id.etTitleInput))
            .perform(typeText("This will completely not work!"))

        // check if button is disabled
        onView(withId(R.id.create_column))
            .check(matches(withText(R.string.create)))
            .check((matches(not(isEnabled()))))

        // Check if error appears
        onView(withId(R.id.etTitle))
            .check(matches(hasTextInputLayoutErrorText(expectedMaxLengthError)))

        // Clean input
        onView(withId(R.id.etTitleInput))
            .perform(clearText())

        // check if button is enabled
        onView(withId(R.id.create_column))
            .check(matches(withText(R.string.create)))
            .check(matches(not(isEnabled())))

        // Check if error appear
        onView(withId(R.id.etTitle))
            .check(matches((hasTextInputLayoutErrorText(expectedRequiredError))))

        // Close dialog
        onView(withId(R.id.cancel_create_column))
            .check(matches(isEnabled()))
            .perform(click())

    }


    @Test
    fun shouldOpenDialog_And_Cancel() {
        // simply opens dialog
        openDialogAndCheckIfItsOpen()

        // check if button is enabled and click
        onView(withId(R.id.cancel_create_column))
            .check(matches(withText(R.string.cancel)))
            .check(matches(isEnabled()))
            .perform(click())
    }

    @Test
    fun shouldCreateAnd_Display_NewColumn() {
        openDialogAndCheckIfItsOpen()
        val newColumnName = "Doing"

        // Type and Check if we can update
        onView(withId(R.id.etTitleInput))
            .perform(typeText(newColumnName))

        // check if button is enabled and click
        onView(withId(R.id.create_column))
            .check(matches(withText(R.string.create)))
            .check((matches(isEnabled())))
            .perform(click())

        // test if its displayed
        onView(withId(R.id.viewpager))
            .perform(swipeLeft())

        onView(allOf(isDisplayed(), withText("Doing")))
            .check(matches(isDisplayed()))
    }

    private fun openDialogAndCheckIfItsOpen() {
        onView(withId(R.id.action_new_column))
            .perform(click())

        // Check if dialog is open by searching for a create button
        val dialogCreateButton = rule.activity.getString(R.string.create)
        onView(withText(dialogCreateButton))
            .check(matches(not(isEnabled())))
    }
}