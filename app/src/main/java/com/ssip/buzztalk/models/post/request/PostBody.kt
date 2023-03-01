package com.ssip.buzztalk.models.post.request

data class PostBody(
    val content: String,
    val taggedUsers: List<String>,
    val hashTags: List<String>,
    val postsFor: List<String>,
    val isImage: Boolean = false,
    val imageUrl: String = ""
)
