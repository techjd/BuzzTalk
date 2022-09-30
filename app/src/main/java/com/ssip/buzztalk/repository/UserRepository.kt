package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.UserAPI
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    suspend fun getUserInfo(token: String) = userAPI.getUserInfo(token)

    suspend fun getAllSearchUsers() = userAPI.getAllUsers()
}