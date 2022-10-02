package com.ssip.buzztalk.models.chat.response.conversations

data class Conversation(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val lastMessage: String,
    val recipientObj: List<RecipientObj>,
    val recipients: List<String>,
    val updatedAt: String
)