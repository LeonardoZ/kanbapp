package com.github.leonardoz.kanbapp.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import com.github.leonardoz.kanbapp.data.entity.ColumnType.*
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import java.lang.IllegalArgumentException
import javax.inject.Inject

class BoardViewModel @Inject constructor(
    private val boardsRepository: BoardsRepository,
    private val columnsRepository: ColumnsRepository,
    context: Context
) : ViewModel() {

    val boardId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }

    val board: LiveData<Board?> = Transformations.switchMap(boardId) {
        boardsRepository.getBoard(it)
    }

    val columns: LiveData<List<Column>> = Transformations.switchMap(board) { loaded ->
        loaded?.let { columnsRepository.getAllColumns(loaded.id) } ?: MutableLiveData()
    }

    val onAddColumn = SingleLiveEvent<Long>()
    val onManageColumns = SingleLiveEvent<Long>()
    private val todo: String? = context.getString(R.string.todo)
    private val done: String? = context.getString(R.string.done)
    private val basicColumnChecks = listOf(
        Pair(TODO_COLUMN, true), Pair(DONE_COLUMN, true)
    )

    /** check if board has column
     * Yes -> Load into column
     * No -> Create Todo Column and Done Column
     */
    fun onColumnsLoaded() {
        val columns: List<Column>? = columns.value
        val boardId = boardId.value
        requireNotNull(columns)
        requireNotNull(boardId)

        val missingColumns = checkIfBoardHasBasicColumns(columns)
            .filter { (_, isMissing) -> isMissing }
            .map { (type, _) -> createColumn(boardId, type) }

        when (missingColumns.size) {
            2 -> columnsRepository.saveColumns(missingColumns[0], missingColumns[1])
        }
    }

    private fun checkIfBoardHasBasicColumns(columns: List<Column>?): List<Pair<ColumnType, Boolean>> =
        if (columns == null || columns.isEmpty())
            basicColumnChecks
        else
            listOf()


    private fun createColumn(boardId: Long, columnType: ColumnType): Column {
        requireNotNull(todo)
        requireNotNull(done)

        val (title, order) = when (columnType) {
            TODO_COLUMN -> Pair(todo, 1)
            DONE_COLUMN -> Pair(done, 2)
            else -> throw IllegalArgumentException("Invalid Column Type")
        }

        return Column(title = title, order = order, boardId = boardId, type = columnType)
    }


    /**
     * Change column position
     * Change WORK_COLUMN order
     */
    fun reorderWorkColumns() {
        onManageColumns.postValue(boardId.value)
    }

    /**
     * Add Column
     * Can only add column of type ColumnType.WORK_COLUMN
     */
    fun addColumn() {
        onAddColumn.postValue(boardId.value)
    }

    /**
     * Remove Column
     * Can only delete column of type ColumnType.WORK_COLUMN
     */
    fun removeColumn() {

    }

    /**
     * Change Column
     * Can change column title, but not the type
     */
    fun changeColumn() {

    }

}
