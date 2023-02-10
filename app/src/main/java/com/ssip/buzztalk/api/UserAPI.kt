package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.models.auth.user.login.request.UserRequestLogin
import com.ssip.buzztalk.models.auth.user.login.response.UserLoginResponse
import com.ssip.buzztalk.models.auth.user.register.request.UserRequestRegister
import com.ssip.buzztalk.models.auth.user.register.response.UserRegisterResponse
import com.ssip.buzztalk.models.chat.response.ChatAPIResponse
import com.ssip.buzztalk.models.connections.request.ToId
import com.ssip.buzztalk.models.connections.response.Connections
import com.ssip.buzztalk.models.connections.response.allConnections.AllConnections
import com.ssip.buzztalk.models.connections.response.connectionrequests.ConnectionRequests
import com.ssip.buzztalk.models.connections.response.requestId.RequestId
import com.ssip.buzztalk.models.followUnfollow.request.Followee
import com.ssip.buzztalk.models.followUnfollow.response.FollowUnfollow
import com.ssip.buzztalk.models.followers.request.FolloweeId
import com.ssip.buzztalk.models.followers.response.FollowerId
import com.ssip.buzztalk.models.followers.response.Followers
import com.ssip.buzztalk.models.following.response.Followings
import com.ssip.buzztalk.models.notifications.request.NotificationBody
import com.ssip.buzztalk.models.notifications.response.UserNotifications
import com.ssip.buzztalk.models.searchusers.request.OtherUserInfoRequest
import com.ssip.buzztalk.models.searchusers.response.SearchUsers
import com.ssip.buzztalk.models.searchusers.response.User
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.models.totalCount.response.FollowersFollowingCount
import com.ssip.buzztalk.models.totalCount.response.Following
import com.ssip.buzztalk.models.user.response.UserInfo
import com.ssip.buzztalk.models.usernames.UserNames
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
    suspend fun getAllFollowers(@Header("Authorization") token: String, @Body followeeId: FolloweeId): Response<Followers>

    @POST(Constants.GET_ALL_FOLLOWING)
    suspend fun getAllFollowing(@Header("Authorization") token: String, @Body followerId: com.ssip.buzztalk.models.following.request.FollowerId): Response<Followings>

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

    @POST(Constants.SEND_NOTI_TOKEN)
    suspend fun sendNotiToken(@Header("Authorization") token: String, @Body notificationBody: NotificationBody): Response<DefaultJSONResponse>

    @GET(Constants.GET_NOTIFICATIONS)
    suspend fun getNotifications(@Header("Authorization") token: String): Response<UserNotifications>
}