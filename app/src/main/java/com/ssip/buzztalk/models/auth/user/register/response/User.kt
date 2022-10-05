package com.ssip.buzztalk.models.auth.user.register.response

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val updatedAt: String
)