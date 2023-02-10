package com.ssip.buzztalk.models.organizations.request

data class RegisterRequest(
  val orgName: String,
  val orgType: String,
  val orgBio: String,
  val orgImage: String = "",
  val orgWebSite: String,
  val orgPhone: String,
  val orgEmail: String,
  val orgPwd: String,
  val orgUserName: String
)
