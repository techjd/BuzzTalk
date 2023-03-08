package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.models.chat.request.MakeUserOnline
import com.ssip.buzztalk.models.chat.request.MessageBody
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.models.chat.response.ChatAPIResponse
import com.ssip.buzztalk.models.chat.response.conversations.Conversations
import com.ssip.buzztalk.models.chat.response.messages.Messages
import com.ssip.buzztalk.models.groupchat.request.CreateNewGroupRequest
import com.ssip.buzztalk.models.groupchat.request.SendMessageGroupRequest
import com.ssip.buzztalk.models.groupchat.response.allgroups.AllGroupsResponse
import com.ssip.buzztalk.models.groupchat.response.groupmessages.GroupMessages
import com.ssip.buzztalk.models.groupchat.response.singlegroupinfo.SingleGroupInfo
import com.ssip.buzztalk.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatAPI {
    @POST(Constants.MAKE_ME_ONLINE)
    suspend fun makeMeOnline(@Body makeUserOnline: MakeUserOnline): Response<ChatAPIResponse>

    @DELETE(Constants.REMOVE_ME_ONLINE)
    suspend fun removeMeOnline(): Response<ChatAPIResponse>

    @POST(Constants.SEND_MESSAGE)
    suspend fun sendMessage(@Body messageBody: MessageBody): Response<ChatAPIResponse>

    @POST(Constants.GET_ALL_MESSAGES)
    suspend fun getAllMessages(@Body to: To): Response<Messages>

    @GET(Constants.GET_ALL_CONVERSATIONS)
    suspend fun getAllConversation(): Response<Conversations>

    @POST(Constants.GET_USER_STATUS)
    suspend fun getUserStatus(@Body to: To): Response<ChatAPIResponse>

    @POST(Constants.CREATE_GROUP)
    suspend fun createGroup(@Body createNewGroupRequest: CreateNewGroupRequest): Response<DefaultJSONResponse>

    @GET(Constants.GET_GROUPS)
    suspend fun getGroups(): Response<AllGroupsResponse>

    @POST(Constants.SEND_GROUP_MESSAGE)
    suspend fun sendGroupMessage(
        @Body sendMessageGroupRequest: SendMessageGroupRequest,
        @Path("groupId") groupId: String
    ): Response<DefaultJSONResponse>

    @POST(Constants.GET_GROUP_MESSAGE)
    suspend fun getGroupMessages(
        @Path("groupId") groupId: String
    ): Response<GroupMessages>

    @GET(Constants.GET_SINGLE_GROUP_INFO)
    suspend fun getSingleGroupInfo(
        @Path("groupId") groupId: String
    ): Response<SingleGroupInfo>
}