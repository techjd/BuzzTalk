package com.ssip.buzztalk.models.groupchat.response.singlegroupinfo

data class Data(
    val groupDetails: GroupDetails,
    val groupMembers: List<GroupMember>
)