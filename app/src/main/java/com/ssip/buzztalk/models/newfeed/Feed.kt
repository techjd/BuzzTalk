package com.ssip.buzztalk.models.newfeed

data class Feed(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val feedContent: String,
    val feedTitle: String,
    val postedByCompany: PostedByCompany,
    val postedByUniversity: PostedByUniversity,
    val updatedAt: String,
    val userId: String
)