package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.UserAPI
import com.ssip.buzztalk.models.followUnfollow.request.Followee
import com.ssip.buzztalk.models.searchusers.request.OtherUserInfoRequest
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    suspend fun getUserInfo(token: String) = userAPI.getUserInfo(token)

    suspend fun getOthersInfo(token: String, otherUserInfoRequest: OtherUserInfoRequest) =
        userAPI.getUserInfoOfOtherUsers(token, otherUserInfoRequest)

    suspend fun getAllSearchUsers() = userAPI.getAllUsers()

    suspend fun followUser(token: String, followee: Followee) = userAPI.followUser(token, followee)

    suspend fun unFollowUser(token: String) = userAPI.unfollowUser(token)
}