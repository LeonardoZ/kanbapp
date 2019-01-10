package com.github.leonardoz.kanbapp.view.fragment


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
import com.github.leonardoz.kanbapp.util.BaseUiTest
import com.github.leonardoz.kanbapp.util.RecyclerViewMatcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BoardsListFragmentTest : BaseUiTest() {

    @get:Rule
    private val rule: ActivityTestRule<BoardsActivity> = ActivityTestRule(
        BoardsActivity::class.java,
        false,
        true
    )
    private val boardA = Board(name = "Weekly Goals")
    private val boardB = Board(name = "Task Board")
    private val boardC = Board(name = "Dish-washing Kanban")
    private lateinit var boardsDao: BoardsDao

    init {
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                as? KanbappApplication)?.let {
            boardsDao = it.appComponent.exposeBoardsDao()
        }
    }

    @Before
    fun prepare() {
        insertData()
        rule.launchActivity(null)
    }

    @After
    fun clean() {
        removeData()
    }

    @Test
    fun shouldDisplay_ThreeItems() {
        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(1, R.id.board_title)
        ).check(matches(withText("Task Board")))

        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.board_title)
        ).check(matches(withText("Dish-washing Kanban")))

    }

    @Test
    fun shouldRemove_Board_AndDisplay_TwoItems() {

        // lets data be inserted
        rule.finishActivity()
        rule.launchActivity(null)

        // open popup and click
        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.ib_popup_menu)
        ).perform(click())

        onView(withText(rule.activity.getString(R.string.remove)))
            .inRoot(withDecorView(not(`is`(rule.activity.window.decorView))))
            .perform(click())

        // click into the dialog confirm button
        onView(withText(R.string.remove))
            .perform(click())

        // 0pos item was removed
        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.board_title)
        ).check(matches(withText("Task Board")))
    }

    @Test
    fun shouldDisplay_EmptyMessage() {
        removeData()

        onView(withId(R.id.emptyView))
            .check(matches(isDisplayed()))
    }

    private fun insertData() {
        removeData()
        val ids = boardsDao.insertBoards(boardA, boardB, boardC)
        boardA.id = ids[0]
        boardB.id = ids[1]
        boardC.id = ids[2]

    }

    private fun removeData() {
        boardsDao.deleteBoard(boardA)
        boardsDao.deleteBoard(boardB)
        boardsDao.deleteBoard(boardC)
    }


}
