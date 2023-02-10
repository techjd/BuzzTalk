package com.ssip.buzztalk.models.organizations.response.register

data class Organization(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val organizationBio: String,
    val organizationEmail: String,
    val organizationImage: String,
    val organizationName: String,
    val organizationPassword: String,
    val organizationPhone: String,
    val organizationType: String,
    val organizationUserName: String,
    val organizationWebSite: String,
    val updatedAt: String
)