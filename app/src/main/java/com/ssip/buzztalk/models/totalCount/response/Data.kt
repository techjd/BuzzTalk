package com.ssip.buzztalk.models.totalCount.response

data class Data(
    val followers: List<Follower>,
    val following: List<Following>
)