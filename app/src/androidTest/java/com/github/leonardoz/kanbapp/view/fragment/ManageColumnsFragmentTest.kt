package com.github.leonardoz.kanbapp.view.fragment

import androidx.core.view.DragStartHelper
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import com.github.leonardoz.kanbapp.data.entity.ColumnType.*
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.AndroidAsyncTask
import com.github.leonardoz.kanbapp.util.BaseUiTest
import com.github.leonardoz.kanbapp.util.RecyclerViewMatcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.ViewAction


@LargeTest
@RunWith(AndroidJUnit4::class)
class ManageColumnsFragmentTest : BaseUiTest() {

    // Base Activity
    @get:Rule
    private val rule: ActivityTestRule<BoardsActivity> =
        ActivityTestRule(
            BoardsActivity::class.java,
            false,
            true
        )
    private var boardDao: BoardsDao
    private var columnDao: ColumnsDao
    private var columnsRepository: ColumnsRepository

    private val board = Board(name = "Weekly Goals")
    private var columnA = Column(
        title = "Doing",
        boardId = board.id,
        order = 2,
        type = WORK_COLUMN
    )
    private var columnB = Column(
        title = "Testing",
        boardId = board.id,
        order = 3,
        type = WORK_COLUMN
    )

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

        insertAdditionalColumns()

        onView(withId(R.id.action_manage))
            .perform(click())
    }

    @After
    fun clean() {
        removeData()
    }

    @Test
    fun should_Display_Four_Items() {
        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(0, R.id.columnTitle)
        ).check(matches(withText(R.string.todo)))

        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(1, R.id.columnTitle)
        ).check(matches(withText("Doing")))

        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(2, R.id.columnTitle)
        ).check(matches(withText("Testing")))

        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(3, R.id.columnTitle)
        ).check(matches(withText(R.string.done)))
    }

    @Test
    fun shouldRemove_And_Display_Four_Items() {
        // open dialog
        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(2, R.id.btnRemoveColumn)
        ).perform(click())

        // confirm remove
        onView(withText(rule.activity.getString(R.string.remove_column)))
            .inRoot(withDecorView(not(`is`(rule.activity.window.decorView))))
            .perform(click())

        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPositionOnView(2, R.id.columnTitle)
        ).check(matches(withText(R.string.done)))
    }

    @Test
    fun should_Reorder_On_DragNDrop() {
        onView(
            RecyclerViewMatcher(R.id.manageColumnsRecyclerView)
                .atPosition(1)
        ).perform(swipeDown())

        // TODO

    }

    fun swipeDown(): ViewAction {
        return GeneralSwipeAction(
            Swipe.SLOW, GeneralLocation.TOP_CENTER,
            GeneralLocation.BOTTOM_CENTER, Press.FINGER
        )
    }

    private fun insertBoard() {
        board.id = boardDao.insertBoard(board)
        columnA.boardId = board.id
        columnB.boardId = board.id
    }

    private fun insertAdditionalColumns() {
        columnsRepository.saveColumnChangingOrder(columnA, columnB)
    }

    private fun removeData() {
        boardDao.deleteBoard(board)
        columnsRepository.deleteColumn(columnA)
        columnsRepository.deleteColumn(columnB)
    }

}