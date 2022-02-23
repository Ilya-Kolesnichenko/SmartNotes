package com.ilyakoles.smartnotes.di.folders

import com.ilyakoles.smartnotes.data.repository.UserRepositoryImpl
import com.ilyakoles.smartnotes.di.ApplicationScope
import com.ilyakoles.smartnotes.domain.users.IUserRepository
import dagger.Binds
import dagger.Module

@Module
interface FolderDataModule {
    @Binds
    @ApplicationScope
    fun bindUserRepository(impl: UserRepositoryImpl): IUserRepository
}
