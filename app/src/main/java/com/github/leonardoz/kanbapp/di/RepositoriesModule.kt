package com.github.leonardoz.kanbapp.di

import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.dao.ColumnsDao
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.AndroidAsyncTask
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Singleton
    @Provides
    fun providesBoardsRepository(boardsDao: BoardsDao, asyncTask: AndroidAsyncTask) =
        BoardsRepository(boardsDao, asyncTask)

    @Singleton
    @Provides
    fun providesColumnRepository(columnsDao: ColumnsDao, asyncTask: AndroidAsyncTask) =
        ColumnsRepository(columnsDao, asyncTask)

}
