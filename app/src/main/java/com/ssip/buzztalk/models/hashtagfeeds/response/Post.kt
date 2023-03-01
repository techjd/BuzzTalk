package com.ssip.buzztalk.models.hashtagfeeds.response

data class Post(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val hashtagId: HashtagId,
    val postId: PostId,
    val updatedAt: String
)