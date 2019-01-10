package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.getValue
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class ColumnViewModelTest {

    private val columnRepository = mock(ColumnsRepository::class.java)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldLoad_Column() {
        val boardId = 10L
        val columnId = 20L

        // Load data

        val board = Board(id = boardId, name = "Daily Diary")
        val column = Column(
            id = columnId,
            title = "Doing",
            boardId = boardId,
            type = ColumnType.WORK_COLUMN
        )

        val boardData = MutableLiveData<Board?>()
        boardData.postValue(board)

        val columnData = MutableLiveData<Column?>()
        columnData.postValue(column)

        `when`(columnRepository.getColumn(boardId, columnId)).thenReturn(columnData)

        // test
        val viewModel = ColumnViewModel(columnRepository)
        viewModel.boardAndColumnId.postValue(Pair(boardId, columnId))
        getValue(viewModel.column)

        verify(columnRepository).getColumn(boardId, columnId)
    }

}