package com.ssip.buzztalk.ui.fragments.connections

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.connections.request.ToId
import com.ssip.buzztalk.models.connections.response.Connections
import com.ssip.buzztalk.models.connections.response.connectionrequests.ConnectionRequests
import com.ssip.buzztalk.models.connections.response.requestId.RequestId
import com.ssip.buzztalk.models.user.response.UserInfo
import com.ssip.buzztalk.repository.UserRepository
import com.ssip.buzztalk.utils.ErrorResponse
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class ConnectionRequestsViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val userRepository: UserRepository,
    private val errorResponse: ErrorResponse
): ViewModel() {

    private val _allConnectionRequests: MutableLiveData<NetworkResult<ConnectionRequests>> = MutableLiveData()
    val allConnectionRequests: LiveData<NetworkResult<ConnectionRequests>> = _allConnectionRequests

    private val _acceptRequest: MutableLiveData<NetworkResult<Connections>> = MutableLiveData()
    val acceptRequest: LiveData<NetworkResult<Connections>> = _acceptRequest

    fun getAllConnectionsRequests(token: String) {
        viewModelScope.launch {
            _allConnectionRequests.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.getAllConnectionsRequests(token)
                    if (data.isSuccessful) {
                        _allConnectionRequests.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred ${data.errorBody()}")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _allConnectionRequests.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _allConnectionRequests.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _allConnectionRequests.postValue(NetworkResult.Error("Network Failure"))
                    else -> _allConnectionRequests.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }

    fun acceptRequest(token: String, requestId: RequestId) {
        viewModelScope.launch {
            _acceptRequest.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.acceptRequest(token, requestId)
                    if (data.isSuccessful) {
                        _acceptRequest.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred ${data.errorBody()}")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _acceptRequest.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _acceptRequest.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _acceptRequest.postValue(NetworkResult.Error("Network Failure"))
                    else -> _acceptRequest.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later $t"))
                }
            }
        }
    }
}












