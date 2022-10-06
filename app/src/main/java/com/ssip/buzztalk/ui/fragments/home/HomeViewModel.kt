package com.ssip.buzztalk.ui.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.models.notifications.request.NotificationBody
import com.ssip.buzztalk.repository.UserRepository
import com.ssip.buzztalk.utils.ErrorResponse
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkManager: NetworkManager,
    private val errorResponse: ErrorResponse,
    ): ViewModel() {

    private val _sendNoti: MutableLiveData<NetworkResult<DefaultJSONResponse>> = MutableLiveData()
    val sendNot: LiveData<NetworkResult<DefaultJSONResponse>> = _sendNoti

    fun sendNoti(token : String, notificationBody: NotificationBody) {
        viewModelScope.launch {
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.sendNotiToken(token, notificationBody)
                    if (data.isSuccessful) {
                        _sendNoti.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _sendNoti.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _sendNoti.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _sendNoti.postValue(NetworkResult.Error("Network Failure"))
                    else -> _sendNoti.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }
}