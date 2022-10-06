package com.ssip.buzztalk.models.following.response

data class Following(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val followeeId: FolloweeId,
    val followerId: String,
    val updatedAt: String
)