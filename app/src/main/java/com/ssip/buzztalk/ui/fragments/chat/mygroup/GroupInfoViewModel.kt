package com.ssip.buzztalk.ui.fragments.chat.mygroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.groupchat.response.singlegroupinfo.SingleGroupInfo
import com.ssip.buzztalk.repository.ChatRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class GroupInfoViewModel @Inject constructor(
  private val chatRepository: ChatRepository
): ViewModel() {

  private val _singleGroupInfo: MutableLiveData<NetworkResult<SingleGroupInfo>> = MutableLiveData()
  val singleGroupInfo: LiveData<NetworkResult<SingleGroupInfo>> = _singleGroupInfo

  fun getSingleGroupInfo(groupId: String) {
    viewModelScope.launch {
      _singleGroupInfo.postValue(NetworkResult.Loading())
      _singleGroupInfo.postValue(chatRepository.getSingleGroupInfo(groupId))
    }
  }
}