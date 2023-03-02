package com.ssip.buzztalk.models.groupchat.response

data class AllGroup(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val groupId: GroupId,
    val updatedAt: String,
    val userId: String
)