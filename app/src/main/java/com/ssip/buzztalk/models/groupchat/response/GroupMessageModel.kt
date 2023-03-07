package com.ssip.buzztalk.models.groupchat.response

data class GroupMessageModel(
  val fromId: String,
  val fromName: String,
  val messageContent: String
)
