package com.ilyakoles.smartnotes.domain.users

import javax.inject.Inject

class LoginedUserUseCase @Inject constructor(private val repository: IUserRepository) {

    operator suspend fun invoke(login: String, password: String) = repository.logined(login, password)

}