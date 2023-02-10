package com.ssip.buzztalk.models.comment.response

data class Data(
    val __v: Int,
    val _id: String,
    val content: String,
    val createdAt: String,
    val postId: String,
    val updatedAt: String,
    val userId: UserId
)