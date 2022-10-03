package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.chat.request.MakeUserOnline
import com.ssip.buzztalk.models.chat.request.MessageBody
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.models.chat.response.ChatAPIResponse
import com.ssip.buzztalk.models.chat.response.conversations.Conversations
import com.ssip.buzztalk.models.chat.response.messages.Messages
import com.ssip.buzztalk.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

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
}