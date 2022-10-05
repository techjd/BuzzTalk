package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.auth.user.login.request.UserRequestLogin
import com.ssip.buzztalk.models.auth.user.login.response.UserLoginResponse
import com.ssip.buzztalk.models.auth.user.register.request.UserRequestRegister
import com.ssip.buzztalk.models.auth.user.register.response.UserRegisterResponse
import com.ssip.buzztalk.models.connections.request.ToId
import com.ssip.buzztalk.models.connections.response.Connections
import com.ssip.buzztalk.models.connections.response.allConnections.AllConnections
import com.ssip.buzztalk.models.connections.response.connectionrequests.ConnectionRequests
import com.ssip.buzztalk.models.connections.response.requestId.RequestId
import com.ssip.buzztalk.models.followUnfollow.request.Followee
import com.ssip.buzztalk.models.followUnfollow.response.FollowUnfollow
import com.ssip.buzztalk.models.searchusers.request.OtherUserInfoRequest
import com.ssip.buzztalk.models.searchusers.response.SearchUsers
import com.ssip.buzztalk.models.searchusers.response.User
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.models.totalCount.response.FollowersFollowingCount
import com.ssip.buzztalk.models.user.response.UserInfo
import com.ssip.buzztalk.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    @GET(Constants.GET_USER_INFO_ENDPOINT)
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserInfo>

    @POST(Constants.GET_USER_INFO_OF_OTHER_USERS_ENDPOINT)
    suspend fun getUserInfoOfOtherUsers(@Header("Authorization") token: String, @Body otherUserInfoRequest: OtherUserInfoRequest): Response<UserInfo>

    @GET(Constants.GET_SEARCH_USERS)
    suspend fun getAllUsers(): Response<SearchUsers>

    @POST(Constants.FOLLOW_USER)
    suspend fun followUser(@Header("Authorization") token: String, @Body followee: Followee): Response<FollowUnfollow>

    @POST(Constants.UNFOLLOW_USER)
    suspend fun unfollowUser(@Header("Authorization") token: String, @Body followee: Followee): Response<FollowUnfollow>

    @POST(Constants.GET_ALL_FOLLOWERS_AND_FOLLOWING)
    suspend fun getAllFollowersAndFollowing(@Header("Authorization") token: String, @Body userID: UserID): Response<FollowersFollowingCount>

    @POST(Constants.GET_ALL_CONNECTIONS)
    suspend fun getAllConnections(@Header("Authorization") token: String, @Body userID: UserID): Response<AllConnections>

    @POST(Constants.GET_ALL_FOLLOWERS)
    suspend fun getAllFollowers(@Header("Authorization") token: String, )

    @POST(Constants.CHECK_IF_USER_FOLLOWED_OR_NOT)
    suspend fun checkIfUserFollowedOrNot(@Header("Authorization") token: String, @Body followee: Followee): Response<FollowUnfollow>

    @POST(Constants.SEND_REQUEST)
    suspend fun sendRequest(@Header("Authorization") token: String,@Body toId: ToId): Response<Connections>

    @POST(Constants.CHECK_IF_REQUEST_SENT_OR_NOT)
    suspend fun checkIfRequestSentOrNot(@Header("Authorization") token: String,@Body toId: ToId): Response<Connections>

    @GET(Constants.GET_ALL_CONNECTIONS_REQUESTS)
    suspend fun getAllConnectionsRequests(@Header("Authorization") token: String): Response<ConnectionRequests>

    @PUT(Constants.ACCEPT_REQUEST)
    suspend fun acceptRequest(@Header("Authorization") token: String,@Body requestId: RequestId): Response<Connections>

}