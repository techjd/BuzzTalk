package com.ssip.buzztalk.ui.fragments.userdetailprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.followUnfollow.request.Followee
import com.ssip.buzztalk.models.followUnfollow.response.FollowUnfollow
import com.ssip.buzztalk.models.searchusers.request.OtherUserInfoRequest
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
class UserDetailProfileViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val userRepository: UserRepository,
    private val errorResponse: ErrorResponse
): ViewModel(){

    private val _otherUserInfo: MutableLiveData<NetworkResult<UserInfo>> = MutableLiveData()
    val otherUserInfo: LiveData<NetworkResult<UserInfo>> = _otherUserInfo

    private val _followUser: MutableLiveData<NetworkResult<FollowUnfollow>> = MutableLiveData()
    val followUser: LiveData<NetworkResult<FollowUnfollow>> = _followUser

    private val _unFollowUser: MutableLiveData<NetworkResult<FollowUnfollow>> = MutableLiveData()
    val unFollowUser: LiveData<NetworkResult<FollowUnfollow>> = _unFollowUser

    fun getInfo(token: String, otherUserInfoRequest: OtherUserInfoRequest) {
        viewModelScope.launch {
            _otherUserInfo.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.getOthersInfo(token, otherUserInfoRequest)
                    if (data.isSuccessful) {
                        _otherUserInfo.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _otherUserInfo.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _otherUserInfo.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _otherUserInfo.postValue(NetworkResult.Error("Network Failure"))
                    else -> _otherUserInfo.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later ${t.toString()}"))
                }
            }
        }
    }

    fun followUser(token: String, followee: Followee) {
        viewModelScope.launch {
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.followUser(token, followee)
                    if (data.isSuccessful) {
                        _followUser.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _followUser.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _followUser.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _followUser.postValue(NetworkResult.Error("Network Failure"))
                    else -> _followUser.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }

    fun unFollowUser(token: String) {
        viewModelScope.launch {
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.unFollowUser(token)
                    if (data.isSuccessful) {
                        _unFollowUser.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _unFollowUser.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _unFollowUser.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _unFollowUser.postValue(NetworkResult.Error("Network Failure"))
                    else -> _unFollowUser.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }
}