package com.ssip.buzztalk.models.opportunities.response

data class Opportunity(
    val __v: Int,
    val _id: String,
    val budget: String,
    val date: String,
    val lookingFor: String,
    val orgId: OrgId,
    val postsFor: List<String>,
    val requiredSkills: String,
    val shortDescription: String
)