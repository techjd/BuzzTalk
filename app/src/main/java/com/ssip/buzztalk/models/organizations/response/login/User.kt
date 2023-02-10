package com.ssip.buzztalk.models.organizations.response.login

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val organizationBio: String,
    val organizationEmail: String,
    val organizationName: String,
    val organizationPassword: String,
    val organizationPhone: String,
    val organizationType: String,
    val organizationUserName: String,
    val organizationWebSite: String,
    val updatedAt: String
)