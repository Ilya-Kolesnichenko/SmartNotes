package com.ilyakoles.smartnotes.di.folders

import android.app.Application
import com.ilyakoles.smartnotes.data.network.IXMLAnswerHolderApi
import com.ilyakoles.smartnotes.data.network.NetworkFactory
import com.ilyakoles.smartnotes.data.repository.FolderRepositoryImpl
import com.ilyakoles.smartnotes.di.ApplicationScope
import com.ilyakoles.smartnotes.domain.folders.IFolderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface FolderDataModule {

    @Binds
    @ApplicationScope
    fun bindFolderRepository(impl: FolderRepositoryImpl): IFolderRepository

}
