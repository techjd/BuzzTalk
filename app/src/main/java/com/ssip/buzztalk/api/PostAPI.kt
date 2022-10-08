package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.models.feed.response.Feed
import com.ssip.buzztalk.models.newfeed.NewFeed
import com.ssip.buzztalk.models.post.request.PostBody
import com.ssip.buzztalk.models.post.response.PostAPIResponse
import com.ssip.buzztalk.models.usernames.UserNames
import com.ssip.buzztalk.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PostAPI {
    @POST(Constants.POST_CONTENT)
    suspend fun addPost(@Body postBody: PostBody): Response<PostAPIResponse>

    @GET(Constants.GET_USERNAMES)
    suspend fun getUserNames(): Response<UserNames>

    @GET(Constants.GET_FEED)
    suspend fun getFeed(): Response<Feed>

    @GET(Constants.GET_NEW_FEED)
    suspend fun getNewFeed(): Response<NewFeed>

    @POST(Constants.POST_NEW_OPPO_COMPANY)
    suspend fun postNewOpportunitiesCompany(@Body newOpportunities: newOpportunities): Response<DefaultJSONResponse>

    @POST(Constants.POST_NEW_OPPO_UNIVERSITY)
    suspend fun postNewOpportunitiesUniversity(@Body newOpportunities: newOpportunities): Response<DefaultJSONResponse>
}

data class newOpportunities(
    val postTitle: String,
    val postContent: String,
    val to: List<String>
)