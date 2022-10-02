package com.ssip.buzztalk.models.chat.response.messages

data class Messages(
    val `data`: Data,
    val message: String,
    val status: String
)