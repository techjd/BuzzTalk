package com.ssip.buzztalk.ui.fragments.chat.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssip.buzztalk.models.chat.MessageModel
import com.ssip.buzztalk.models.chat.request.MessageBody
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.models.chat.response.ChatAPIResponse
import com.ssip.buzztalk.models.chat.response.conversations.Conversations
import com.ssip.buzztalk.models.chat.response.messages.Messages
import com.ssip.buzztalk.models.chat.response.newmessage.NewMessage
import com.ssip.buzztalk.repository.ChatRepository
import com.ssip.buzztalk.utils.Constants
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val chatRepository: ChatRepository): ViewModel() {

    private val _allMessages: MutableLiveData<NetworkResult<Messages>> = MutableLiveData()
    val allMessages: LiveData<NetworkResult<Messages>> = _allMessages

    private val _sendMessage: MutableLiveData<NetworkResult<ChatAPIResponse>> = MutableLiveData()
    val sendMessage: LiveData<NetworkResult<ChatAPIResponse>> = _sendMessage

    fun getAllMessages(to: To) {
        viewModelScope.launch {
            _allMessages.postValue(NetworkResult.Loading())
            _allMessages.postValue(chatRepository.getAllMessages(to))
        }
    }

    fun sendMessage(messageBody: MessageBody) {
        viewModelScope.launch {
            _sendMessage.postValue(chatRepository.sendMessage(messageBody))
        }
    }

//    val messages = flow {
//        var messageForRecyclerView: MessageModel? = null
//        socket.off(Constants.NEW_MESSAGE).on(Constants.NEW_MESSAGE) { message ->
//            val message = gson.fromJson(message[0].toString(), NewMessage::class.java)
//            messageForRecyclerView = MessageModel(message.data.message.from, message.data.message.body)
//        }
//        if (messageForRecyclerView != null) {
//            emit(messageForRecyclerView)
//        }
//    }

}