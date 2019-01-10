package com.github.leonardoz.kanbapp.view.dialog


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.github.leonardoz.kanbapp.BoardsActivity
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.BoardRestrictions
import com.github.leonardoz.kanbapp.util.BaseUiTest
import com.github.leonardoz.kanbapp.util.RecyclerViewMatcher
import com.github.leonardoz.kanbapp.util.format
import com.github.leonardoz.kanbapp.util.hasTextInputLayoutErrorText
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.*
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ChangeNameDialogTest : BaseUiTest() {

    @get:Rule
    private val rule: ActivityTestRule<BoardsActivity> = ActivityTestRule(
        BoardsActivity::class.java,
        false,
        true
    )
    private var context = InstrumentationRegistry.getInstrumentation().targetContext
    private val boardA = Board(name = "Weekly Goals")
    private lateinit var boardsDao: BoardsDao

    init {
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                as? KanbappApplication)?.let {
            boardsDao = it.appComponent.exposeBoardsDao()
        }
    }

    @Before
    fun startActivity() {
        insertData()
        rule.launchActivity(null)
    }

    @After
    fun clean() {
        removeData()
    }

    @Test
    fun shouldOpenDialog_andChangeName_Errors() {
        openAndCheckIfDialogIsOpen()

        val expectedMaxLengthError = format(context.getString(R.string.max_length), BoardRestrictions.nameSizeLimit)
        val expectedRequiredError = context.getString(R.string.required)

        // Type invalid text
        onView(withId(R.id.etNameInput))
            .perform(typeText("This will completely explode!"))

        // check if button is disabled
        onView(withId(R.id.change_name))
            .check(matches(withText(R.string.change)))
            .check((matches(not(isEnabled()))))

        // Check if error appears
        onView(withId(R.id.etName))
            .check(matches(hasTextInputLayoutErrorText(expectedMaxLengthError)))

        // Clean input
        onView(withId(R.id.etNameInput))
            .perform(clearText())

        // check if button is enabled
        onView(withId(R.id.change_name))
            .check(matches(withText(R.string.change)))
            .check(matches(not(isEnabled())))

        // Check if error appear
        onView(withId(R.id.etName))
            .check(matches((hasTextInputLayoutErrorText(expectedRequiredError))))

        // Close dialog
        onView(withId(R.id.cancel_change_name))
            .check(matches(isEnabled()))
            .perform(click())
    }


    @Test
    fun shouldOpenDialog_And_Cancel() {
        // simply opens dialog
        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.ib_popup_menu)
        ).perform(click())

        onView(withText(rule.activity.getString(R.string.change_name)))
            .inRoot(withDecorView(not(`is`(rule.activity.window.decorView))))
            .perform(click())


        // check if button is enabled and click
        onView(withId(R.id.cancel_change_name))
            .check(matches(withText(R.string.cancel)))
            .check(matches(isEnabled()))
            .perform(click())
    }

    @Test
    fun shouldUpdate_AndDisplayUpdated_Board() {
        openAndCheckIfDialogIsOpen()
        val newBoardName = "Monthly Goals"

        // Type and Check if we can update
        onView(withId(R.id.etNameInput))
            .perform(typeText(newBoardName))

        // check if button is enabled and click
        onView(withId(R.id.change_name))
            .check(matches(withText(R.string.change)))
            .check((matches(isEnabled())))
            .perform(click())

        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.board_title)
        ).check(matches(withText(newBoardName)))

        // for other tests that depends on checking the dialog name
        boardA.name = newBoardName
    }

    private fun openAndCheckIfDialogIsOpen() {
        // open dialog
        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.ib_popup_menu)
        ).perform(click())

        onView(withText(rule.activity.getString(R.string.change_name)))
            .inRoot(withDecorView(not(`is`(rule.activity.window.decorView))))
            .perform(click())

        // Check if dialog is open by searching for a change button
        val dialogChangeButton = rule.activity.getString(R.string.change)
        onView(withText(dialogChangeButton))
            .check(matches(not(isEnabled())))
    }

    private fun insertData() {
        boardA.id = boardsDao.insertBoard(boardA)
    }

    private fun removeData() {
        boardsDao.deleteBoard(boardA)
    }


}