package com.ilyakoles.smartnotes.di.folders

import android.app.Application
import com.ilyakoles.smartnotes.di.ApplicationScope
import com.ilyakoles.smartnotes.di.ViewModelModule
import com.ilyakoles.smartnotes.di.users.UserDataModule
import com.ilyakoles.smartnotes.presentation.*
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        FolderDataModule::class,
        ViewModelModule::class
    ]
)
interface AppFolderComponents {

    fun inject(activity: FoldersActivity)
    fun inject(fragment: FoldersFragment)
    fun inject(fragment: CalendarFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): AppFolderComponents
    }
}
