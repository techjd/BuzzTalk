package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.CompanyAPI
import com.ssip.buzztalk.models.organizations.request.LoginRequest
import com.ssip.buzztalk.models.organizations.request.RegisterRequest
import javax.inject.Inject

class CompanyRepository @Inject constructor(
  private val companyAPI: CompanyAPI
): BaseRepository() {

  suspend fun loginOrg(loginRequest: LoginRequest) = safeApiCall {
      companyAPI.loginOrg(loginRequest)
  }

  suspend fun registerOrg(registerRequest: RegisterRequest) = safeApiCall {
    companyAPI.registerOrg(registerRequest)
  }

  suspend fun getOrgInfo() = safeApiCall {
    companyAPI.getOrgInfo()
  }
}