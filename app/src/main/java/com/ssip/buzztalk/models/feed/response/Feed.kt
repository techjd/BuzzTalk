package com.ssip.buzztalk.models.feed.response

data class Feed(
    val `data`: Data,
    val message: String,
    val status: String
)