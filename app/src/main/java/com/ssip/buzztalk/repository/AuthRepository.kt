package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.AuthAPI
import com.ssip.buzztalk.models.auth.user.login.request.UserRequestLogin
import com.ssip.buzztalk.models.auth.user.register.request.UserRequestRegister
import javax.inject.Inject

class AuthRepository @Inject constructor(private val userAPI: AuthAPI) {

    suspend fun signIn(userRequestLogin: UserRequestLogin) = userAPI.logIn(userRequestLogin)

    suspend fun signUp(userRequestRegister: UserRequestRegister) =
        userAPI.register(userRequestRegister)
}