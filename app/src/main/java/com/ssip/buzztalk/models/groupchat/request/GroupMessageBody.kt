package com.ssip.buzztalk.models.groupchat.request

data class GroupMessageBody(
  val fromId: String,
  val fromName: String,
  val messageContent: String
)
