package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.feed.response.Feed
import com.ssip.buzztalk.models.post.request.PostBody
import com.ssip.buzztalk.models.post.response.PostAPIResponse
import com.ssip.buzztalk.models.usernames.UserNames
import com.ssip.buzztalk.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrganizationAPI {

  @POST(Constants.POST_CONTENT)
  suspend fun addPost(@Body postBody: PostBody): Response<PostAPIResponse>

  @GET(Constants.GET_USERNAMES)
  suspend fun getUserNames(): Response<UserNames>

  @GET(Constants.GET_FEED)
  suspend fun getFeed(): Response<Feed>
}