package com.ilyakoles.smartnotes.domain.users

data class User (
    val login: String?,
    val password: String?,
    val nicName: String?,
    val lastName: String?,
    val firstName: String?,
    val email: String?
)