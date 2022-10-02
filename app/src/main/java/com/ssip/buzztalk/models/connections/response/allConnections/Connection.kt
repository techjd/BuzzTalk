package com.ssip.buzztalk.models.connections.response.allConnections

data class Connection(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val from: From,
    val status: String,
    val to: To,
    val updatedAt: String
)