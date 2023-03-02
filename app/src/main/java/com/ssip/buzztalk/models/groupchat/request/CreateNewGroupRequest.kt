package com.ssip.buzztalk.models.groupchat.request

data class CreateNewGroupRequest(
  val groupName: String,
  val groupImage: String = "",
  val groupBio: String,
  val groupUsers: List<String>
)
