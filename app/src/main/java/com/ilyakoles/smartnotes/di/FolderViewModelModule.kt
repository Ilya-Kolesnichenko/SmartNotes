package com.ilyakoles.smartnotes.di

import androidx.lifecycle.ViewModel
import com.ilyakoles.smartnotes.presentation.viewmodels.FolderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FolderViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FolderViewModel::class)
    fun bindFolderViewModel(viewModel: FolderViewModel): ViewModel
}
