package com.ssip.buzztalk.models.groupchat.response.singlegroupinfo

data class GroupMember(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val groupId: String,
    val updatedAt: String,
    val userId: UserId
)