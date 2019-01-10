package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnRestrictions
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.*
import com.github.leonardoz.kanbapp.view.form.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class ChangeColumnViewModelTest {

    private val repository = mock(ColumnsRepository::class.java)
    private val context = mockContext()
    private val validationFactory = FormValidatorFactory(context)
    private val afterColumnChanged =
        mock(SingleLiveEvent::class.java) as SingleLiveEvent<Any>
    private val cancelColumnChanged =
        mock(SingleLiveEvent::class.java) as SingleLiveEvent<Any>
    private val boardFakeId = 10L
    private val columnFakeId = 20L

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        mockMessages(context)
    }

    @Test
    fun should_InitiateCorrectly() {
        val viewModel = createViewModel()
        viewModel.setToInitialState()
        val changeColumn = ChangeColumn()
        val changeColumnError = ChangeColumnError()
        assertEquals(changeColumn, viewModel.changeColumn.value)
        assertEquals(changeColumnError, viewModel.changeColumnError.value)

        val (boardId, columnId) = getValue(viewModel.boardAndColumnIds)
        assertEquals(boardFakeId, boardId)
        assertEquals(columnFakeId, columnId)
    }

    @Test
    fun should_ReactToChangeText_And_ValidateErrors() {
        val viewModel = createViewModel()
        val newTitle = "Doing a long and irregular text-length phase"

        // Has errors scenario
        viewModel.onTitleChanged(newTitle, 0, 0, 0)
        val postedChangeTitleError = viewModel.changeColumnError.value
        val expected = format(
            context.getString(R.string.max_length),
            ColumnRestrictions.titleSizeLimit
        )
        assertEquals(expected, postedChangeTitleError?.titleError)
        assertEquals(true, postedChangeTitleError?.titleIsWrong)
        assertEquals(false, postedChangeTitleError?.valid)
    }

    @Test
    fun should_ReactToChangeText_And_Validate_WithoutErrors() {
        val viewModel = createViewModel()
        val newTitle = "Doing"

        // Has no errors scenario
        viewModel.onTitleChanged(newTitle, 0, 0, 0)
        val postedChangeTitleError = viewModel.changeColumnError.value

        assertEquals("", postedChangeTitleError?.titleError)
        assertEquals(false, postedChangeTitleError?.titleIsWrong)
        assertEquals(true, postedChangeTitleError?.valid)
    }

    @Test
    fun should_CallSave_Column() {
        val viewModel = createViewModel()
        // load basic data
        val column = Column(
            id = columnFakeId,
            title = "Doing",
            boardId = boardFakeId,
            type = ColumnType.WORK_COLUMN
        )
        val data = MediatorLiveData<Column?>()
        data.postValue(column)
        `when`(repository.getColumn(boardFakeId, columnFakeId)).thenReturn(data)

        viewModel.boardAndColumnIds.postValue(Pair(boardFakeId, columnFakeId))

        assertNotNull(getValue(viewModel.column))

        // test changes
        viewModel.onTitleChanged("Doing", 0, 0, 0)
        viewModel.changeColumn.postValue(ChangeColumn("Doing"))

        doNothing().`when`(repository).updateColumnTitle(safeAny())

        viewModel.changeColumn()

        verify(repository).updateColumnTitle(safeAny())
        verify(afterColumnChanged).call()
    }

    @Test
    fun should_NotTryTo_ChangeColumn() {
        val viewModel = createViewModel()
        viewModel.setToInitialState()
        // Without post
        viewModel.changeColumn()

        verifyZeroInteractions(repository)
        verifyZeroInteractions(afterColumnChanged)
    }

    @Test(expected = IllegalArgumentException::class)
    fun should_NotTryTo_ChangeColumn_WithoutId() {
        val viewModel = createViewModel()

        viewModel.onTitleChanged("Doing", 0, 0, 0)
        viewModel.changeColumn.postValue(ChangeColumn("Doing"))

        viewModel.boardAndColumnIds.postValue(Pair(0, 0))

        // Without id
        viewModel.changeColumn()
    }


    @Test
    fun should_Cancel() {
        val viewModel = createViewModel()

        viewModel.cancelChange()

        verify(cancelColumnChanged).call()
    }

    private fun createViewModel(): ChangeColumnViewModel {
        val viewModel = ChangeColumnViewModel(repository, validationFactory)
        viewModel.boardAndColumnIds.postValue(Pair(boardFakeId, columnFakeId))
        viewModel.afterColumnChanged = afterColumnChanged
        viewModel.cancelColumnChanged = cancelColumnChanged
        return viewModel
    }
}