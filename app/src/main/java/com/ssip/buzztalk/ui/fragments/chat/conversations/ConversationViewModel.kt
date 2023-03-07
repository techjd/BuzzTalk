package com.ssip.buzztalk.ui.fragments.chat.conversations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.chat.response.conversations.Conversations
import com.ssip.buzztalk.models.groupchat.response.allgroups.AllGroupsResponse
import com.ssip.buzztalk.models.groupchat.response.groupmessages.GroupMessages
import com.ssip.buzztalk.repository.ChatRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(private val chatRepository: ChatRepository): ViewModel() {

    private val _allConversations: MutableLiveData<NetworkResult<Conversations>> = MutableLiveData()
    val allConversations: LiveData<NetworkResult<Conversations>> = _allConversations

    private val _allGroups: MutableLiveData<NetworkResult<AllGroupsResponse>> = MutableLiveData()
    val allGroups: LiveData<NetworkResult<AllGroupsResponse>> = _allGroups

    private val _allGroupConvo: MutableLiveData<NetworkResult<GroupMessages>> = MutableLiveData()
    val allGroupConvo: LiveData<NetworkResult<GroupMessages>> = _allGroupConvo

    fun getAllConversations() {
        viewModelScope.launch {
            _allConversations.postValue(NetworkResult.Loading())
            _allConversations.postValue(chatRepository.getAllConversation())
        }
    }

    fun getAllGroups() {
        viewModelScope.launch {
            _allGroups.postValue(NetworkResult.Loading())
            _allGroups.postValue(chatRepository.getGroups())
        }
    }

    fun getAllGroupConvo(groupId: String) {
        viewModelScope.launch {
            _allGroupConvo.postValue(NetworkResult.Loading())
            _allGroupConvo.postValue(chatRepository.getGroupMessages(groupId))
        }
    }
}