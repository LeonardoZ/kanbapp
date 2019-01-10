package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType.*
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.getValue
import com.github.leonardoz.kanbapp.util.safeAny
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class ManageColumnsViewModelTest {

    private val columnRepository = mock(ColumnsRepository::class.java)
    private val boardId = 10L
    private val columns = listOf(
        Column(id = 1, boardId = boardId, title = "TODO", type = TODO_COLUMN),
        Column(id = 2, boardId = boardId, title = "Doing", type = WORK_COLUMN),
        Column(id = 3, boardId = boardId, title = "Done", type = DONE_COLUMN)
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldLoad_AllColumns() {
        val viewModel = createViewModel()
        viewModel.boardId.postValue(boardId)

        val data = MutableLiveData<List<Column>>()
        data.postValue(columns)

        `when`(columnRepository.getAllColumns(boardId)).thenReturn(data)

        val returned = getValue(viewModel.columns)
        assertEquals(returned.size, 3)
    }

    @Test
    fun shouldCall_Update_OnReorder() {
        val viewModel = createViewModel()
        viewModel.onReorder(columns[0], columns[1])
        verify(columnRepository).updateColumnOrders(columns[0], columns[1])
    }

    @Test
    fun shouldCall_Delete_OnRemove() {
        val viewModel = createViewModel()
        viewModel.onRemove(columns.first())
        verify(columnRepository).deleteColumn(columns.first())
    }

    private fun createViewModel() =
        ManageColumnsViewModel(columnRepository)
}