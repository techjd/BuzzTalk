package com.ssip.buzztalk.models.totalCount.response

data class Follower(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val followeeId: String,
    val followerId: String,
    val updatedAt: String
)