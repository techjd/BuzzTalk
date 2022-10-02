package com.ssip.buzztalk.models.chat.response.messages

data class Message(
    val __v: Int,
    val _id: String,
    val body: String,
    val conversation: String,
    val createdAt: String,
    val from: String,
    val fromObj: List<FromObj>,
    val to: String,
    val toObj: List<ToObj>,
    val updatedAt: String
)