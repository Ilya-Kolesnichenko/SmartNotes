package com.ilyakoles.smartnotes.di

import android.app.Activity
import android.app.Application
import com.ilyakoles.smartnotes.di.users.UserDataModule
import com.ilyakoles.smartnotes.presentation.LoginActivity
import com.ilyakoles.smartnotes.presentation.LoginFragment
import com.ilyakoles.smartnotes.presentation.NewUserFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        UserDataModule::class,
        ViewModelModule::class
    ]
)
interface AppUserComponents {

    fun inject(activity: LoginActivity)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: NewUserFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): AppUserComponents
    }
}
