package com.github.leonardoz.kanbapp.view.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
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
import com.github.leonardoz.kanbapp.util.BaseUiTest
import com.github.leonardoz.kanbapp.util.RecyclerViewMatcher
import org.hamcrest.Matchers.allOf
import org.junit.*
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BoardFragmentTest : BaseUiTest() {

    // Base Activity
    @get:Rule
    private val rule: ActivityTestRule<BoardsActivity> =
        ActivityTestRule(
            BoardsActivity::class.java,
            false,
            true
        )
    private lateinit var boardsDao: BoardsDao
    private lateinit var columnDao: ColumnsDao

    private val board = Board(name = "Weekly Goals")

    init {
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                as? KanbappApplication)?.let {
            boardsDao = it.appComponent.exposeBoardsDao()
            columnDao = it.appComponent.exposeColumnsDao()
        }
    }


    @Before
    fun prepareData() {
        board.id = boardsDao.insertBoard(board)

        // launch activity and open the board
        rule.finishActivity()
        rule.launchActivity(null)

        onView(
            RecyclerViewMatcher(R.id.boardsRecyclerView)
                .atPositionOnView(0, R.id.btnOpenBoard)
        ).perform(click())
    }

    @After
    fun clear() {
        boardsDao.deleteBoard(board)
    }

    @Test
    fun onLoad_ShouldHave_TodoAndDone_Columns() {
        onView(allOf(isDisplayed(), withText(R.string.todo)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.viewpager))
            .perform(swipeLeft())

        onView(allOf(isDisplayed(), withText(R.string.done)))
            .check(matches(isDisplayed()))
    }

}