package com.ilyakoles.smartnotes.presentation

import android.app.Application
import androidx.work.Configuration
import com.ilyakoles.smartnotes.di.DaggerAppUserComponents
import com.ilyakoles.smartnotes.di.folders.DaggerAppFolderComponents
import javax.inject.Inject

class SmartNotesApp : Application() {
    val component by lazy {
        DaggerAppUserComponents.factory().create(this)
    }

    val componentFolders by lazy {
        DaggerAppFolderComponents.factory().create(this)
    }
}

