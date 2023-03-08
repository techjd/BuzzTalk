package com.ssip.buzztalk.models.groupchat.response.newgroupmessage

data class Message(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val groupId: String,
    val message: String,
    val updatedAt: String,
    val userId: UserId
)