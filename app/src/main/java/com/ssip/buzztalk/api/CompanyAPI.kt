package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.feed.response.Feed
import com.ssip.buzztalk.models.organizations.request.LoginRequest
import com.ssip.buzztalk.models.organizations.request.RegisterRequest
import com.ssip.buzztalk.models.organizations.response.info.OrgInfo
import com.ssip.buzztalk.models.organizations.response.login.OrgLoginInfo
import com.ssip.buzztalk.models.organizations.response.register.OrgRegisterResponse
import com.ssip.buzztalk.models.post.request.PostBody
import com.ssip.buzztalk.models.post.response.PostAPIResponse
import com.ssip.buzztalk.models.usernames.UserNames
import com.ssip.buzztalk.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CompanyAPI {
    @POST(Constants.ORG_LOGIN)
    suspend fun loginOrg(@Body loginRequest: LoginRequest): Response<OrgLoginInfo>

    @GET(Constants.ORG_REGISTER)
    suspend fun registerOrg(@Body registerRequest: RegisterRequest): Response<OrgRegisterResponse>

    @GET(Constants.GET_ORG_INFO)
    suspend fun getOrgInfo(): Response<OrgInfo>
}