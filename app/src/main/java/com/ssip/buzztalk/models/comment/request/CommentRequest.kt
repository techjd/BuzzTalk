package com.ssip.buzztalk.models.comment.request

data class CommentRequest(
  val content: String,
  val postId: String
)