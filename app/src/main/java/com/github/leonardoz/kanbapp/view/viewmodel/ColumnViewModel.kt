package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import javax.inject.Inject

class ColumnViewModel @Inject constructor(
    private val columnsRepository: ColumnsRepository
) : ViewModel() {

    val boardAndColumnId = MutableLiveData<Pair<Long, Long>>()

    val column: LiveData<Column?>? = Transformations.switchMap(boardAndColumnId) { pair ->
        columnsRepository.getColumn(pair.first, pair.second)
    }

}
