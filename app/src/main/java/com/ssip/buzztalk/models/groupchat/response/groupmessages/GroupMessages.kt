package com.ssip.buzztalk.models.groupchat.response.groupmessages

data class GroupMessages(
    val `data`: Data,
    val message: String,
    val status: String
)