package com.ssip.buzztalk.models.auth.user.register.request

data class UserRequestRegister(
    val userName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String
)