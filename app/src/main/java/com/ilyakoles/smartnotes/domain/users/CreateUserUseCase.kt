package com.ilyakoles.smartnotes.domain.users

import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val repository: IUserRepository) {
    operator suspend fun invoke(login: String, password: String, nicName: String?, lastName: String?,
                        firstName: String?, email: String?) =
        repository.createUser(login, password, nicName, lastName, firstName, email)
}