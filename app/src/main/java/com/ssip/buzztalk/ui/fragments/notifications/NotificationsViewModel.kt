package com.ssip.buzztalk.ui.fragments.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.notifications.response.UserNotifications
import com.ssip.buzztalk.repository.UserRepository
import com.ssip.buzztalk.utils.ErrorResponse
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationsViewModel @Inject constructor(
  private val userRepository: UserRepository,
  private val networkManager: NetworkManager,
  private val errorResponse: ErrorResponse
): ViewModel() {

  private val _userNotifications: MutableLiveData<NetworkResult<UserNotifications>> = MutableLiveData()
  val userNotifications: LiveData<NetworkResult<UserNotifications>> = _userNotifications

  fun getUserNotifications(token: String) {
    viewModelScope.launch {
      _userNotifications.postValue(NetworkResult.Loading())
      try {
        if (networkManager.hasInternetConnection()) {
          val data = userRepository.getUserNotifications(token)
          if (data.isSuccessful) {
            _userNotifications.postValue(NetworkResult.Success(data.body()!!))
          } else {
            Log.d("ERROR", "getUserInfo: Some Error Occurred")
            val error = errorResponse.giveErrorResult(data.errorBody()!!)
            _userNotifications.postValue(NetworkResult.Error(error.message))
          }
        } else {
          _userNotifications.postValue(NetworkResult.Error("No Internet Connection"))
        }
      } catch (t: Throwable) {
        when (t) {
          is IOException -> _userNotifications.postValue(NetworkResult.Error("Network Failure"))
          else -> _userNotifications.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
        }
      }
    }
  }
}