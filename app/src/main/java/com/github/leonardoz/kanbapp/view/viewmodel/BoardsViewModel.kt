package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import javax.inject.Inject

class BoardsViewModel @Inject constructor(val repository: BoardsRepository) : ViewModel() {

    val boards: LiveData<List<Board>> = repository.getAllBoards()
    var navigateToCreateBoardFragment = SingleLiveEvent<Any>()
    var informBoardWasRemoved = SingleLiveEvent<Board>()

    fun onNewBoardClicked() {
        navigateToCreateBoardFragment.call()
    }

    // TODO Better possible errors handling
    fun removeBoard(board: Board) {
        repository.deleteBoard(board)
        informBoardWasRemoved.value = board
    }

}