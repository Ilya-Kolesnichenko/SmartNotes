package com.ilyakoles.smartnotes.domain.users

import com.ilyakoles.smartnotes.domain.Answer

interface IUserRepository {

    suspend fun logined(login: String, password: String): Answer?

    suspend fun createUser(login: String, password: String, nicName: String?, lastName: String?,
                           firstName: String?, email: String?) : Answer?
}