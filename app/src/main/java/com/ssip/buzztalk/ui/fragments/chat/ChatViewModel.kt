package com.ssip.buzztalk.ui.fragments.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.chat.request.MakeUserOnline
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.models.chat.response.ChatAPIResponse
import com.ssip.buzztalk.repository.ChatRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository): ViewModel() {

    private val _makeMeOnline: MutableLiveData<NetworkResult<ChatAPIResponse>> = MutableLiveData()
    val makeMeOnline: LiveData<NetworkResult<ChatAPIResponse>> = _makeMeOnline

    private val _removeMeOnline: MutableLiveData<NetworkResult<ChatAPIResponse>> = MutableLiveData()
    val removeMeOnline: LiveData<NetworkResult<ChatAPIResponse>> = _removeMeOnline

    private val _userStatus: MutableLiveData<NetworkResult<ChatAPIResponse>> = MutableLiveData()
    val userStatus: LiveData<NetworkResult<ChatAPIResponse>> = _userStatus

    fun makeMeOnline(makeUserOnline: MakeUserOnline) {
        viewModelScope.launch {
            _makeMeOnline.postValue(NetworkResult.Loading())
            _makeMeOnline.postValue(chatRepository.makeMeOnline(makeUserOnline))
        }
    }

    fun removeMeOnline() {
        viewModelScope.launch {
            chatRepository.removeMeOnline()
        }
    }

    fun getUserStatus(to: To) {
        viewModelScope.launch {
            _userStatus.postValue(NetworkResult.Loading())
            _userStatus.postValue(chatRepository.getUserStatus(to))
        }
    }
}