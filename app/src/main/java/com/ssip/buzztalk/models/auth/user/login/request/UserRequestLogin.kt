package com.ssip.buzztalk.models.auth.user.login.request

data class UserRequestLogin(
    val email: String,
    val password: String
)