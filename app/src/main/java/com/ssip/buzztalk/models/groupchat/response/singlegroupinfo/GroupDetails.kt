package com.ssip.buzztalk.models.groupchat.response.singlegroupinfo

data class GroupDetails(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val createdBy: String,
    val groupBio: String,
    val groupImage: String,
    val groupName: String,
    val lastMessage: String,
    val updatedAt: String
)