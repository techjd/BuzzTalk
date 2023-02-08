package com.ssip.buzztalk.models.notifications.response

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val postId: PostId,
    val text: String,
    val updatedAt: String,
    val userId: String
)