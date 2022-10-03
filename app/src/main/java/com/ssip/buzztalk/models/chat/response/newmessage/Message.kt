package com.ssip.buzztalk.models.chat.response.newmessage

data class Message(
    val __v: Int,
    val _id: String,
    val body: String,
    val conversation: String,
    val createdAt: String,
    val from: String,
    val to: String,
    val updatedAt: String
)