package com.ilyakoles.smartnotes.di

import androidx.lifecycle.ViewModel
import com.ilyakoles.smartnotes.presentation.viewmodels.FolderViewModel
import com.ilyakoles.smartnotes.presentation.viewmodels.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface UserViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    fun bindUserViewModel(viewModel: UserViewModel): ViewModel
}



