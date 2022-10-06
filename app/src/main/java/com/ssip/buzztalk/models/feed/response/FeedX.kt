package com.ssip.buzztalk.models.feed.response

data class FeedX(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val postId: PostId,
    val toId: String,
    val type: String,
    val updatedAt: String,
    val userId: String
)