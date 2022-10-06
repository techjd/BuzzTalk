package com.ssip.buzztalk.models.followers.response

data class Follower(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val followeeId: String,
    val followerId: FollowerId,
    val updatedAt: String
)