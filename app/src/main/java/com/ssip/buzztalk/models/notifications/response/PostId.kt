package com.ssip.buzztalk.models.notifications.response

data class PostId(
    val __v: Int,
    val _id: String,
    val comments: Int,
    val content: String,
    val createdAt: String,
    val likes: Int,
    val postsFor: List<Any>,
    val updatedAt: String,
    val userId: UserId
)