package com.github.leonardoz.kanbapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.leonardoz.kanbapp.view.viewmodel.BoardsViewModel
import com.github.leonardoz.kanbapp.view.viewmodel.ChangeNameViewModel
import com.github.leonardoz.kanbapp.view.viewmodel.CreateBoardViewModel
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
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}