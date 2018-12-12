package com.github.leonardoz.kanbapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.leonardoz.kanbapp.data.entity.Board

@Dao
interface BoardsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBoard(board: Board): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBoards(vararg boards: Board): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateBoard(board: Board)

    @Delete
    fun deleteBoard(board: Board)

    @Query("SELECT * FROM boards ORDER BY name")
    fun getAllBoards(): LiveData<List<Board>>

    @Query("SELECT * FROM boards WHERE name LIKE '%'||:name||'%' ORDER BY name")
    fun getAllBoards(name: String): LiveData<List<Board>>

    @Query("SELECT * FROM boards WHERE id = :id")
    fun getBoard(id: Long): LiveData<Board?>

}