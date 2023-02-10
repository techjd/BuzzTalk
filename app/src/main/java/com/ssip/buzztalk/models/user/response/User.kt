package com.ssip.buzztalk.models.user.response

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val bio : String,
    val updatedAt: String,
    val userType: String
)