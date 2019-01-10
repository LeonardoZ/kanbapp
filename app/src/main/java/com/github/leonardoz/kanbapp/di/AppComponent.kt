package com.github.leonardoz.kanbapp.di

import com.github.leonardoz.kanbapp.BoardsActivity
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.dao.ColumnsDao
import com.github.leonardoz.kanbapp.view.dialog.ChangeColumnDialog
import com.github.leonardoz.kanbapp.view.dialog.ChangeNameDialog
import com.github.leonardoz.kanbapp.view.dialog.CreateColumnDialog
import com.github.leonardoz.kanbapp.view.fragment.ManageColumnsFragment
import com.github.leonardoz.kanbapp.view.fragment.BoardFragment
import com.github.leonardoz.kanbapp.view.fragment.BoardsListFragment
import com.github.leonardoz.kanbapp.view.fragment.ColumnFragment
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

    fun injectBoardFragment(boardFragment: BoardFragment)

    fun injectColumnFragment(columnFragment: ColumnFragment)

    fun injectCreateColumnDialog(createColumnDialog: CreateColumnDialog)

    fun injectChangeColumnDialog(changeColumnDialog: ChangeColumnDialog)

    fun injectManageColumnsDialog(manageColumnsFragment: ManageColumnsFragment)

    fun exposeBoardsDao(): BoardsDao

    fun exposeColumnsDao(): ColumnsDao


}