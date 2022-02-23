package com.ilyakoles.smartnotes.di.users

import android.app.Application
import com.ilyakoles.smartnotes.data.repository.UserRepositoryImpl
import com.ilyakoles.smartnotes.di.ApplicationScope
import com.ilyakoles.smartnotes.domain.users.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface UserDataModule {

    @Binds
    @ApplicationScope
    fun bindUserRepository(impl: UserRepositoryImpl): IUserRepository

/*    @Provides
    @ApplicationScope
    fun provideApiService(): ApiService {
        return ApiFactory.apiService
    }*/
}
