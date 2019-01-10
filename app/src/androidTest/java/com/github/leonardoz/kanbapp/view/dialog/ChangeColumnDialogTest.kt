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
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.*
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.allOf
import org.junit.*
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ChangeColumnDialogTest : BaseUiTest() {

    // Base Activity
    @get:Rule
    private val rule: ActivityTestRule<BoardsActivity> =
        ActivityTestRule(
            BoardsActivity::class.java,
            false,
            true
        )
    private var context = InstrumentationRegistry.getInstrumentation().targetContext
    private var boardDao: BoardsDao
    private var columnDao: ColumnsDao

    private var columnsRepository: ColumnsRepository

    private val board = Board(name = "Weekly Goals")


    init {
        val kanbapp: KanbappApplication? =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as? KanbappApplication
        requireNotNull(kanbapp)

        boardDao = kanbapp.appComponent.exposeBoardsDao()
        columnDao = kanbapp.appComponent.exposeColumnsDao()

        columnsRepository = ColumnsRepository(columnDao, AndroidAsyncTask())
    }

    @Before
    fun prepare() {
        insertBoard()

        rule.launchActivity(null)

        // go to fragment
        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.btnOpenBoard)
        ).perform(click())

        onView(withId(R.id.action_manage))
            .perform(click())

        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(0, R.id.columnTitle)
        ).check(matches(withText(R.string.todo)))

    }

    @After
    fun clean() {
        removeData()
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
        onView(withId(R.id.change_column))
            .check(matches(withText(R.string.change)))
            .check((matches(not(isEnabled()))))

        // Check if error appears
        onView(withId(R.id.etTitle))
            .check(matches(hasTextInputLayoutErrorText(expectedMaxLengthError)))

        // Clean input
        onView(withId(R.id.etTitleInput))
            .perform(clearText())

        // check if button is enabled
        onView(withId(R.id.change_column))
            .check(matches(withText(R.string.change)))
            .check(matches(not(isEnabled())))

        // Check if error appear
        onView(withId(R.id.etTitle))
            .check(matches((hasTextInputLayoutErrorText(expectedRequiredError))))

        // Close dialog
        onView(withId(R.id.cancel_change_column))
            .check(matches(isEnabled()))
            .perform(click())

    }


    @Test
    fun shouldOpenDialog_And_Cancel() {
        // simply opens dialog
        openDialogAndCheckIfItsOpen()

        // check if button is enabled and click
        onView(withId(R.id.cancel_change_column))
            .check(matches(withText(R.string.cancel)))
            .check(matches(isEnabled()))
            .perform(click())
    }

    @Test
    fun shouldCreateAnd_Display_NewColumn() {
        openDialogAndCheckIfItsOpen()
        val newColumnName = "TO-DO"

        // Type and Check if we can update
        onView(withId(R.id.etTitleInput))
            .perform(typeText(newColumnName))

        // check if button is enabled and click
        onView(withId(R.id.change_column))
            .check(matches(withText(R.string.change)))
            .check((matches(isEnabled())))
            .perform(click())

        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(0, R.id.columnTitle)
        ).check(matches(withText(newColumnName)))

    }

    private fun openDialogAndCheckIfItsOpen() {
        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(0, R.id.btnEditColumn)
        ).perform(click())

        // Check if dialog is open by searching for a create button
        val dialogChangeButton = rule.activity.getString(R.string.change)

        onView(withText(dialogChangeButton))
            .check(matches(not(isEnabled())))
    }

    private fun insertBoard() {
        board.id = boardDao.insertBoard(board)
    }

    private fun removeData() {
        boardDao.deleteBoard(board)
    }
}