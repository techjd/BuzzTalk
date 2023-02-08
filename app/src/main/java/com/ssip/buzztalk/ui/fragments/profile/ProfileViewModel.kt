package com.ssip.buzztalk.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.connections.response.allConnections.AllConnections
import com.ssip.buzztalk.models.myfeed.response.MyFeed
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.models.totalCount.response.FollowersFollowingCount
import com.ssip.buzztalk.models.user.response.UserInfo
import com.ssip.buzztalk.repository.PostRepository
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
    private val postRepository: PostRepository,
    private val errorResponse: ErrorResponse
    ): ViewModel() {
    private val _userInfo: MutableLiveData<NetworkResult<UserInfo>> = MutableLiveData()
    val userInfo: LiveData<NetworkResult<UserInfo>> = _userInfo

    private val _totalFollowersFollowing: MutableLiveData<NetworkResult<FollowersFollowingCount>> = MutableLiveData()
    val totalFollowersFollowingCount: LiveData<NetworkResult<FollowersFollowingCount>> = _totalFollowersFollowing

    private val _allConnections: MutableLiveData<NetworkResult<AllConnections>> = MutableLiveData()
    val allConnections: LiveData<NetworkResult<AllConnections>> = _allConnections

    private val _myFeed: MutableLiveData<NetworkResult<MyFeed>> = MutableLiveData()
    val myFeed: LiveData<NetworkResult<MyFeed>> = _myFeed
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

    fun getFollowersFollowingCount(token: String, userID: UserID) {
        viewModelScope.launch {
            _totalFollowersFollowing.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.getTotalFollowersFollowing(token, userID)
                    if (data.isSuccessful) {
                        _totalFollowersFollowing.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred ${data.errorBody()}")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _totalFollowersFollowing.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _totalFollowersFollowing.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _totalFollowersFollowing.postValue(NetworkResult.Error("Network Failure"))
                    else -> _totalFollowersFollowing.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }

    fun getAllConnections(token: String, userID: UserID) {
        viewModelScope.launch {
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
                    else -> _allConnections.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later $t"))
                }
            }
        }
    }

    fun getMyFeed() {
        viewModelScope.launch {
            _myFeed.postValue(NetworkResult.Loading())
            _myFeed.postValue(postRepository.getMyFeed())
        }
    }
}