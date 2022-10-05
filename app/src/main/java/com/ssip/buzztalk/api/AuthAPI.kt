package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.auth.user.login.request.UserRequestLogin
import com.ssip.buzztalk.models.auth.user.login.response.UserLoginResponse
import com.ssip.buzztalk.models.auth.user.register.request.UserRequestRegister
import com.ssip.buzztalk.models.auth.user.register.response.UserRegisterResponse
import com.ssip.buzztalk.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {

    @POST(Constants.REGISTER_ENDPOINT)
    suspend fun register(@Body userRequestRegister: UserRequestRegister): Response<UserRegisterResponse>

    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun logIn(@Body userRequestLogin: UserRequestLogin): Response<UserLoginResponse>
}