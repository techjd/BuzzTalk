package com.ssip.buzztalk.models.myfeed.response

data class Feed(
    val __v: Int,
    val _id: String,
    val comments: Int,
    val content: String,
    val createdAt: String,
    val likes: Int,
    val postsFor: List<String>,
    val updatedAt: String,
    val userId: UserId
)