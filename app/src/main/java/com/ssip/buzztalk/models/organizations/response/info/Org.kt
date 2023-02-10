package com.ssip.buzztalk.models.organizations.response.info

data class Org(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val organizationBio: String,
    val organizationEmail: String,
    val organizationName: String,
    val organizationPhone: String,
    val organizationType: String,
    val organizationUserName: String,
    val organizationWebSite: String,
    val updatedAt: String
)