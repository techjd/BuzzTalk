package com.ssip.buzztalk.models.groupchat.response.newgroupmessage

data class NewGroupMessage(
    val `data`: Data,
    val message: String,
    val status: String
)