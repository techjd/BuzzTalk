package com.ssip.buzztalk.models.comment.response

data class AllComments(
    val `data`: List<Data>,
    val message: String,
    val status: String
)