package com.github.leonardoz.kanbapp.data.repository

import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.util.AsyncAction
import java.util.*


// TODO better asyn action, better error handling
open class BoardsRepository(val boardsDao: BoardsDao, val asyncAction: AsyncAction) : ViewModel() {

    open fun saveBoard(board: Board) = asyncAction.execute {
        boardsDao.insertBoard(board)
    }

    open fun deleteBoard(board: Board) = asyncAction.execute {
        boardsDao.deleteBoard(board)
    }

    open fun updateBoard(board: Board) = asyncAction.execute {
        board.updatedAt = Date()
        boardsDao.updateBoard(board)
    }

    open fun getBoard(id: Long) = boardsDao.getBoard(id)

    open fun getAllBoards() = boardsDao.getAllBoards()


}