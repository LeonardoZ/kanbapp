package com.github.leonardoz.kanbapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.leonardoz.kanbapp.view.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BoardsViewModel::class)
    abstract fun bindBoardsViewModel(boardsViewModel: BoardsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateBoardViewModel::class)
    abstract fun bindCreateBoardViewModel(createBoardViewModel: CreateBoardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeNameViewModel::class)
    abstract fun bindChangeNameViewModel(changeNameViewModel: ChangeNameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BoardViewModel::class)
    abstract fun bindBoardViewModel(boardViewModel: BoardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ColumnViewModel::class)
    abstract fun bindColumnViewModel(columnViewModel: ColumnViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateColumnViewModel::class)
    abstract fun bindCreateColumnViewModel(createColumnViewModel: CreateColumnViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeColumnViewModel::class)
    abstract fun bindChangeColumnViewModel(changeColumnViewModel: ChangeColumnViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManageColumnsViewModel::class)
    abstract fun bindManageColumnsViewModel(manageColumnsViewModel: ManageColumnsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}