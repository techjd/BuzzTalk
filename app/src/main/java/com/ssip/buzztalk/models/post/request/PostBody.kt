package com.ssip.buzztalk.models.post.request

data class PostBody(
    val content: String,
    val taggedUsers: List<String>,
    val hashTags: List<String>
)
