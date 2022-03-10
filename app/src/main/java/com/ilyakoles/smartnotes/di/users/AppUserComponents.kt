package com.ilyakoles.smartnotes.di

import android.app.Application
import com.ilyakoles.smartnotes.di.users.UserDataModule
import com.ilyakoles.smartnotes.presentation.login.LoginActivity
import com.ilyakoles.smartnotes.presentation.login.LoginFragment
import com.ilyakoles.smartnotes.presentation.login.NewUserFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        UserDataModule::class,
        UserViewModelModule::class
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
