package com.ssip.buzztalk.ui.fragments.chat.conversations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.chat.response.conversations.Conversations
import com.ssip.buzztalk.repository.ChatRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(private val chatRepository: ChatRepository): ViewModel() {

    private val _allConversations: MutableLiveData<NetworkResult<Conversations>> = MutableLiveData()
    val allConversations: LiveData<NetworkResult<Conversations>> = _allConversations

    fun getAllConversations() {
        viewModelScope.launch {
            _allConversations.postValue(NetworkResult.Loading())
            _allConversations.postValue(chatRepository.getAllConversation())
        }
    }

}