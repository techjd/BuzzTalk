package com.ssip.buzztalk.models.chat.response.conversations

data class RecipientObj(
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val updatedAt: String
)