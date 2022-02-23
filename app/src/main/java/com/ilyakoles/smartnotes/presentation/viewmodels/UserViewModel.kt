package com.ilyakoles.smartnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.ilyakoles.smartnotes.domain.users.CreateUserUseCase
import com.ilyakoles.smartnotes.domain.users.LoginedUserUseCase
import com.ilyakoles.smartnotes.domain.users.User
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val loginedUserUseCase: LoginedUserUseCase,
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    suspend fun logined(login: String, password: String) = loginedUserUseCase(login, password)
    suspend fun createUser(login: String, password: String, nicName: String?, lastName: String?,
                   firstName: String?, email: String?) =
        createUserUseCase(login, password, nicName, lastName, firstName, email)

}
