package com.ssip.buzztalk.models.chat.response.newmessage

data class NewMessage(
    val `data`: Data,
    val message: String,
    val status: String
)