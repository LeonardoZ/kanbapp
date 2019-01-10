package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import javax.inject.Inject

class ManageColumnsViewModel @Inject constructor(
    private val columnsRepository: ColumnsRepository
) : ViewModel() {

    var afterOk = SingleLiveEvent<Any>()
    var canceled = SingleLiveEvent<Any>()

    val boardId by lazy {
        MutableLiveData<Long>()
    }

    val columns: LiveData<List<Column>> = Transformations.switchMap(boardId) {
        columnsRepository.getAllColumns(it)
    }

    fun onRemove(column: Column) {
        columnsRepository.deleteColumn(column)
    }

    fun onReorder(columnA: Column, columnB: Column) {
        columnsRepository.updateColumnOrders(columnA, columnB)
    }

}