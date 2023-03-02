package com.ssip.buzztalk.ui.fragments.newgroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.models.connections.response.allConnections.AllConnections
import com.ssip.buzztalk.models.connections.response.allConnections.Connection
import com.ssip.buzztalk.models.connections.response.allConnections.Data
import com.ssip.buzztalk.models.groupchat.request.CreateNewGroupRequest
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.repository.ChatRepository
import com.ssip.buzztalk.repository.UserRepository
import com.ssip.buzztalk.utils.ErrorResponse
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class GroupViewModel @Inject constructor(
  private val networkManager: NetworkManager,
  private val userRepository: UserRepository,
  private val errorResponse: ErrorResponse,
  private val chatRepository: ChatRepository
) : ViewModel() {

  private val _allConnections: MutableLiveData<NetworkResult<AllConnections>> = MutableLiveData()
  val allConnections: LiveData<NetworkResult<AllConnections>> = _allConnections

  private val _groupName: MutableLiveData<String> = MutableLiveData("")
  val groupName: LiveData<String> = _groupName

  private val _groupDescription: MutableLiveData<String> = MutableLiveData("")
  val groupDescription: LiveData<String> = _groupDescription

  private val _selectedMembers: MutableLiveData<AllConnections> = MutableLiveData(
    AllConnections(
      Data(
        mutableListOf()
      ),
      "",
      ""
    )
  )
  val selectedMembers: LiveData<AllConnections> = _selectedMembers

  private val _createGroupResponse: MutableLiveData<NetworkResult<DefaultJSONResponse>> =
    MutableLiveData()
  val createGroupResponse: LiveData<NetworkResult<DefaultJSONResponse>> = _createGroupResponse

  fun getAllConnections(
    token: String,
    userID: UserID,
  ) {
    viewModelScope.launch {
      if (_selectedMembers.value?.data?.connections!!.isEmpty()) {
        _allConnections.postValue(NetworkResult.Loading())
        try {
          if (networkManager.hasInternetConnection()) {
            val data = userRepository.getAllConnections(token, userID)
            if (data.isSuccessful) {
              _allConnections.postValue(NetworkResult.Success(data.body()!!))
            } else {
              Log.d("ERROR", "getUserInfo: Some Error Occurred ${data.errorBody()}")
              val error = errorResponse.giveErrorResult(data.errorBody()!!)
              _allConnections.postValue(NetworkResult.Error(error.message))
            }
          } else {
            _allConnections.postValue(NetworkResult.Error("No Internet Connection"))
          }
        } catch (t: Throwable) {
          when (t) {
            is IOException -> _allConnections.postValue(NetworkResult.Error("Network Failure"))
            else -> _allConnections.postValue(
              NetworkResult.Error("Some Error Occurred , Please Try Again Later $t")
            )
          }
        }
      }
    }
  }

  fun addMembers(connection: Connection) {
    val selectedMembersList = _selectedMembers.value!!.data.connections
    selectedMembersList.add(connection)

    _selectedMembers.value = _selectedMembers.value?.copy(
      data = Data(selectedMembersList),
      message = _selectedMembers.value!!.message,
      status = _selectedMembers.value!!.status
    )

    val allConnectionsList = _allConnections.value!!.data!!.data.connections
    allConnectionsList.remove(connection)

    _allConnections.value = NetworkResult.Success(
      _allConnections.value!!.data!!.copy(
        data = Data(allConnectionsList),
        message = _allConnections.value!!.data!!.message,
        status = _allConnections.value!!.data!!.status
      )
    )
  }

  fun removeMembers(connection: Connection) {
    val selectedMembersList = _selectedMembers.value!!.data.connections
    selectedMembersList.remove(connection)

    _selectedMembers.value = _selectedMembers.value?.copy(
      data = Data(selectedMembersList),
      message = _selectedMembers.value!!.message,
      status = _selectedMembers.value!!.status
    )

    val allConnectionsList = _allConnections.value!!.data!!.data.connections
    allConnectionsList.add(connection)

    _allConnections.value = NetworkResult.Success(
      _allConnections.value!!.data!!.copy(
        data = Data(allConnectionsList),
        message = _allConnections.value!!.data!!.message,
        status = _allConnections.value!!.data!!.status
      )
    )
  }

  fun saveGroupName(groupName: String) {
    _groupName.value = groupName
  }

  fun saveGroupDescription(groupDesc: String) {
    _groupDescription.value = groupDesc
  }

  fun createGroup(userId: String) {
    val selectedMembers = mutableListOf<String>()
    _selectedMembers.value?.data?.connections?.forEach {
      if (it.from._id == userId) {
        selectedMembers.add(it.to._id)
      } else if(it.to._id == userId){
        selectedMembers.add(it.from._id)
      }
    }
    viewModelScope.launch {
      _createGroupResponse.postValue(NetworkResult.Loading())
      _createGroupResponse.postValue(
        chatRepository.createChatGroup(
          CreateNewGroupRequest(
            groupName = groupName.value!!,
            groupBio = if (groupDescription.value!!.isEmpty()) "" else groupDescription.value!!,
            groupImage = "",
            groupUsers = selectedMembers
          )
        )
      )
    }
  }
}