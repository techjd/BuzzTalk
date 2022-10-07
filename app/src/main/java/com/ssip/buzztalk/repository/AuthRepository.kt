package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.AuthAPI
import com.ssip.buzztalk.models.auth.user.login.request.UserRequestLogin
import com.ssip.buzztalk.models.auth.user.register.request.UserRequestRegister
import com.ssip.buzztalk.ui.fragments.auth.common.UserCompanyRequest
import com.ssip.buzztalk.ui.fragments.auth.common.UserUniversityRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authAPI: AuthAPI) {

    suspend fun signIn(userRequestLogin: UserRequestLogin) = authAPI.logIn(userRequestLogin)

    suspend fun signUp(userRequestRegister: UserRequestRegister) =
        authAPI.register(userRequestRegister)

    suspend fun logInUniversity(universityRequest: UserUniversityRequest) =
        authAPI.loginUniversity(universityRequest)

    suspend fun loginCompany(companyRequest: UserCompanyRequest) =
        authAPI.logInCompany(companyRequest)
}