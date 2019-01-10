package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType.*
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import com.github.leonardoz.kanbapp.util.getValue
import com.github.leonardoz.kanbapp.util.mockContext
import junit.framework.TestCase.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*


@RunWith(JUnit4::class)
class BoardViewModelTest {

    private val boardsRepository = mock(BoardsRepository::class.java)
    private val columnsRepository = mock(ColumnsRepository::class.java)
    private val context = mockContext()
    private val boardId = 42L
    private val board = Board(id = boardId, name = "Daily Tasks")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldLoadData_AfterIdWas_Posted() {
        val viewModel = createViewModel()

        // mock return
        val boardLiveData = MutableLiveData<Board?>()
        boardLiveData.postValue(board)
        `when`(boardsRepository.getBoard(boardId)).thenReturn(boardLiveData)

        // board
        viewModel.boardId.postValue(boardId)

        // Observer triggers transformations
        getValue(viewModel.columns)

        verify(boardsRepository).getBoard(boardId)
        verify(columnsRepository).getAllColumns(boardId)
    }

    @Test
    fun shouldLoad_BasicColumns_WhenEmptyColumns() {
        val viewModel = createViewModel()

        // mock return
        // board
        val boardLiveData = MutableLiveData<Board?>()
        boardLiveData.postValue(board)
        `when`(boardsRepository.getBoard(boardId)).thenReturn(boardLiveData)

        // empty columns
        val columnsData = MutableLiveData<List<Column>>()
        columnsData.postValue(listOf())
        `when`(columnsRepository.getAllColumns(boardId)).thenReturn(columnsData)

        // 0 Columns loaded - needs the basic To-do and Done Columns
        viewModel.boardId.postValue(boardId)
        getValue(viewModel.columns) // trigger changes
        viewModel.onColumnsLoaded() // effective method on test

        assertNotNull(viewModel.columns.value)
        assertTrue(viewModel.columns.value!!.isEmpty())

        val expectedTodo = Column(
            title = context.getString(R.string.todo),
            type = TODO_COLUMN,
            boardId = boardId
        )

        val expectedDone = Column(
            title = context.getString(R.string.done),
            type = DONE_COLUMN,
            order = 2,
            boardId = boardId
        )

        verify(columnsRepository).saveColumns(expectedTodo, expectedDone)
    }

    @Test
    fun shouldNotReload_BasicColumns_WhenNotEmptyColumns() {
        val viewModel = createViewModel()

        // mock return
        // board
        val boardLiveData = MutableLiveData<Board?>()
        boardLiveData.postValue(board)
        `when`(boardsRepository.getBoard(boardId)).thenReturn(boardLiveData)

        // Has columns
        val todo = Column(
            title = context.getString(R.string.todo),
            type = TODO_COLUMN,
            boardId = boardId
        )
        val work = Column(
            title = "Something",
            type = WORK_COLUMN,
            order = 2,
            boardId = boardId
        )
        val done = Column(
            title = context.getString(R.string.done),
            type = DONE_COLUMN,
            order = 3,
            boardId = boardId
        )
        val columnsData = MutableLiveData<List<Column>>()
        columnsData.postValue(listOf(todo, work, done))
        `when`(columnsRepository.getAllColumns(boardId)).thenReturn(columnsData)

        // 3 Columns loaded - doesn't needs the basic To-do and Done Columns
        viewModel.boardId.postValue(boardId)
        getValue(viewModel.columns) // trigger changes
        viewModel.onColumnsLoaded() // effective method on test

        val columns = viewModel.columns.value
        requireNotNull(columns)
        assertFalse(columns.isEmpty())
        assertEquals(3, columns.size)

        verify(columnsRepository, never()).saveColumns(todo, done)
    }

    private fun createViewModel() =
        BoardViewModel(boardsRepository, columnsRepository, context)

}