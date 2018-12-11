package com.github.leonardoz.kanbapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.leonardoz.kanbapp.data.entity.Board

@Dao
interface BoardsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBoard(board: Board)

    @Update
    fun updateBoard(board: Board)

    @Delete
    fun deleteBoard(board: Board)

    @Query("SELECT * FROM boards")
    fun getAllBoards(): LiveData<List<Board>>

    @Query("SELECT * FROM boards WHERE name LIKE :name")
    fun getAllBoardsByName(name: String): LiveData<List<Board>>
}