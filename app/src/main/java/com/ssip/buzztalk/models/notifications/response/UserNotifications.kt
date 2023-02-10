package com.ssip.buzztalk.models.notifications.response

data class UserNotifications(
    val `data`: List<Data>,
    val message: String,
    val status: String
)