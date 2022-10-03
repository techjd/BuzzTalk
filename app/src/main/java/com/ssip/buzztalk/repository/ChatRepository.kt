package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.ChatAPI
import com.ssip.buzztalk.models.chat.request.MakeUserOnline
import com.ssip.buzztalk.models.chat.request.MessageBody
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.utils.NetworkManager
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatAPI: ChatAPI): BaseRepository() {

    suspend fun makeMeOnline(makeUserOnline: MakeUserOnline) = safeApiCall {
        chatAPI.makeMeOnline(makeUserOnline)
    }

    suspend fun removeMeOnline() = safeApiCall {
        chatAPI.removeMeOnline()
    }

    suspend fun sendMessage(messageBody: MessageBody) = safeApiCall {
        chatAPI.sendMessage(messageBody)
    }

    suspend fun getAllMessages(to: To) = safeApiCall {
        chatAPI.getAllMessages(to)
    }

    suspend fun getAllConversation() = safeApiCall {
        chatAPI.getAllConversation()
    }

    suspend fun getUserStatus(to: To) = safeApiCall {
        chatAPI.getUserStatus(to)
    }
}