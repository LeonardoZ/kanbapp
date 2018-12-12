package com.github.leonardoz.kanbapp.di

import com.github.leonardoz.kanbapp.BoardsActivity
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.view.dialog.ChangeNameDialog
import com.github.leonardoz.kanbapp.view.fragment.BoardsListFragment
import com.github.leonardoz.kanbapp.view.fragment.CreateBoardFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, RoomModule::class, RepositoriesModule::class,
        ViewModelModule::class]
)
interface AppComponent {

    fun inject(app: KanbappApplication)

    fun injectActivity(app: BoardsActivity)

    fun injectBoardsListFragment(boardsListFragment: BoardsListFragment)

    fun injectCreateBoardFragment(createBoardFragment: CreateBoardFragment)

    fun injectChangeNameDialog(changeNameDialog: ChangeNameDialog)

    fun exposeBoardsDao(): BoardsDao

}