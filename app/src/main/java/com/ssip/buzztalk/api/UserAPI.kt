package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.auth.user.login.request.UserRequestLogin
import com.ssip.buzztalk.models.auth.user.login.response.UserLoginResponse
import com.ssip.buzztalk.models.auth.user.register.request.UserRequestRegister
import com.ssip.buzztalk.models.auth.user.register.response.UserRegisterResponse
import com.ssip.buzztalk.models.searchusers.response.SearchUsers
import com.ssip.buzztalk.models.user.response.UserInfo
import com.ssip.buzztalk.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserAPI {
    @GET(Constants.GET_USER_INFO_ENDPOINT)
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserInfo>

    @GET(Constants.GET_SEARCH_USERS)
    suspend fun getAllUsers(): Response<SearchUsers>
}