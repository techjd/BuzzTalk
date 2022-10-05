package com.ssip.buzztalk.models.auth.user.login.response

data class UserLoginResponse(
    val `data`: Data,
    val message: String,
    val status: String
)