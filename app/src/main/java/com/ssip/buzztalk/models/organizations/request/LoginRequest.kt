package com.ssip.buzztalk.models.organizations.request

data class LoginRequest(
  val orgEmail: String,
  val orgPwd: String
)
