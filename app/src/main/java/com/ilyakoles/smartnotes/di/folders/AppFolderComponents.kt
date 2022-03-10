package com.ilyakoles.smartnotes.di.folders

import android.app.Application
import com.ilyakoles.smartnotes.di.ApplicationScope
import com.ilyakoles.smartnotes.di.FolderViewModelModule
import com.ilyakoles.smartnotes.presentation.folders.CalendarFragment
import com.ilyakoles.smartnotes.presentation.folders.EditFolderFragment
import com.ilyakoles.smartnotes.presentation.folders.FoldersActivity
import com.ilyakoles.smartnotes.presentation.folders.FoldersFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        FolderDataModule::class,
        FolderViewModelModule::class
    ]
)
interface AppFolderComponents {

    fun inject(activity: FoldersActivity)
    fun inject(fragment: FoldersFragment)
    fun inject(fragment: CalendarFragment)
    fun inject(fragment: EditFolderFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): AppFolderComponents
    }
}
