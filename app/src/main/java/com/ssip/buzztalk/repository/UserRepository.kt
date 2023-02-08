package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.UserAPI
import com.ssip.buzztalk.models.connections.request.ToId
import com.ssip.buzztalk.models.connections.response.requestId.RequestId
import com.ssip.buzztalk.models.followUnfollow.request.Followee
import com.ssip.buzztalk.models.followers.request.FolloweeId
import com.ssip.buzztalk.models.following.request.FollowerId
import com.ssip.buzztalk.models.notifications.request.NotificationBody
import com.ssip.buzztalk.models.searchusers.request.OtherUserInfoRequest
import com.ssip.buzztalk.models.totalCount.request.UserID
import retrofit2.http.Header
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    suspend fun getUserInfo(token: String) = userAPI.getUserInfo(token)

    suspend fun getOthersInfo(token: String, otherUserInfoRequest: OtherUserInfoRequest) =
        userAPI.getUserInfoOfOtherUsers(token, otherUserInfoRequest)

    suspend fun getAllSearchUsers() = userAPI.getAllUsers()

    suspend fun followUser(token: String, followee: Followee) = userAPI.followUser(token, followee)

    suspend fun unFollowUser(token: String, followee: Followee) = userAPI.unfollowUser(token, followee)

    suspend fun checkIfUserIsFollowedOrNot(token: String, followee: Followee) = userAPI.checkIfUserFollowedOrNot(token, followee)

    suspend fun getTotalFollowersFollowing(token: String, userID: UserID) = userAPI.getAllFollowersAndFollowing(token, userID)

    suspend fun sendRequest(token: String, toId: ToId) = userAPI.sendRequest(token, toId)

    suspend fun checkIfRequestSentOrNot(token: String, toId: ToId) = userAPI.checkIfRequestSentOrNot(token, toId)

    suspend fun getAllConnectionsRequests(token: String) = userAPI.getAllConnectionsRequests(token)

    suspend fun acceptRequest(token: String, requestId: RequestId) = userAPI.acceptRequest(token, requestId)

    suspend fun getAllConnections(token: String, userID: UserID) = userAPI.getAllConnections(token, userID)

    suspend fun sendNotiToken(token: String, notificationBody: NotificationBody) = userAPI.sendNotiToken(token, notificationBody)

    suspend fun getAllFollowers(token: String, followeeId: FolloweeId) = userAPI.getAllFollowers(token, followeeId)

    suspend fun getAllFollowing(token: String, followerId: FollowerId) = userAPI.getAllFollowing(token, followerId)

    suspend fun getUserNotifications(token: String) = userAPI.getNotifications(token)
}