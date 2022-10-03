package com.ssip.buzztalk.ui.fragments.chat.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.models.chat.response.conversations.Conversations
import com.ssip.buzztalk.models.chat.response.messages.Messages
import com.ssip.buzztalk.repository.ChatRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val chatRepository: ChatRepository): ViewModel() {

    private val _allMessages: MutableLiveData<NetworkResult<Messages>> = MutableLiveData()
    val allMessages: LiveData<NetworkResult<Messages>> = _allMessages

    fun getAllMessages(to: To) {
        viewModelScope.launch {
            _allMessages.postValue(NetworkResult.Loading())
            _allMessages.postValue(chatRepository.getAllMessages(to))
        }
    }
}