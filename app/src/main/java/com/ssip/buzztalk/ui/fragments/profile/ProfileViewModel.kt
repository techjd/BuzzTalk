package com.ssip.buzztalk.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ProfileViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val userRepository: UserRepository,
    private val errorResponse: ErrorResponse
    ): ViewModel() {
    private val _userInfo: MutableLiveData<NetworkResult<UserInfo>> = MutableLiveData()
    val userInfo: LiveData<NetworkResult<UserInfo>> = _userInfo


    fun getUserInfo(token: String) {
        viewModelScope.launch {
            _userInfo.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.getUserInfo(token)
                    if (data.isSuccessful) {
                        _userInfo.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _userInfo.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _userInfo.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _userInfo.postValue(NetworkResult.Error("Network Failure"))
                    else -> _userInfo.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }
}