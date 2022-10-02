package com.ssip.buzztalk.ui.fragments.userdetailprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.connections.request.ToId
import com.ssip.buzztalk.models.connections.response.Connections
import com.ssip.buzztalk.models.connections.response.allConnections.AllConnections
import com.ssip.buzztalk.models.followUnfollow.request.Followee
import com.ssip.buzztalk.models.followUnfollow.response.FollowUnfollow
import com.ssip.buzztalk.models.searchusers.request.OtherUserInfoRequest
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.models.totalCount.response.FollowersFollowingCount
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

    private val _userFollowedOrNot: MutableLiveData<NetworkResult<FollowUnfollow>> = MutableLiveData()
    val userFollowedOrNot: LiveData<NetworkResult<FollowUnfollow>> = _userFollowedOrNot

    private val _totalFollowersFollowing: MutableLiveData<NetworkResult<FollowersFollowingCount>> = MutableLiveData()
    val totalFollowersFollowingCount: LiveData<NetworkResult<FollowersFollowingCount>> = _totalFollowersFollowing

    private val _sendRequest: MutableLiveData<NetworkResult<Connections>> = MutableLiveData()
    val sendRequest: LiveData<NetworkResult<Connections>> = _sendRequest

    private val _connectionSentOrNot: MutableLiveData<NetworkResult<Connections>> = MutableLiveData()
    val connectionSentOrNot: LiveData<NetworkResult<Connections>> = _connectionSentOrNot

    private val _allConnections: MutableLiveData<NetworkResult<AllConnections>> = MutableLiveData()
    val allConnections: LiveData<NetworkResult<AllConnections>> = _allConnections

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
            _followUser.postValue(NetworkResult.Loading())
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

    fun unFollowUser(token: String, followee: Followee) {
        viewModelScope.launch {
            _unFollowUser.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.unFollowUser(token, followee)
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

    fun checkIfUserIsFollowedOrNot(token: String, followee: Followee) {
        viewModelScope.launch {
            _userFollowedOrNot.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.checkIfUserIsFollowedOrNot(token, followee)
                    Log.d("DATA ", "checkIfUserIsFollowedOrNot: ${data.body()}")
                    if (data.isSuccessful) {
                        _userFollowedOrNot.postValue(NetworkResult.Success(data.body()!!))
                        Log.d("RESPONSE ", "checkIfUserIsFollowedOrNot: ${data.body()}")
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred ${data}")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _userFollowedOrNot.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _userFollowedOrNot.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _userFollowedOrNot.postValue(NetworkResult.Error("Network Failure"))
                    else -> _userFollowedOrNot.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
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

    fun sendRequest(token: String, toId: ToId) {
        viewModelScope.launch {
            _sendRequest.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.sendRequest(token, toId)
                    if (data.isSuccessful) {
                        _sendRequest.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred ${data.errorBody()}")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _sendRequest.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _sendRequest.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _sendRequest.postValue(NetworkResult.Error("Network Failure"))
                    else -> _sendRequest.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }

    fun checkIfUserIsConnectedOrNot(token: String, toId: ToId) {
        viewModelScope.launch {
            _connectionSentOrNot.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.checkIfRequestSentOrNot(token, toId)
                    if (data.isSuccessful) {
                        _connectionSentOrNot.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred ${data.errorBody()}")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _connectionSentOrNot.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _connectionSentOrNot.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _connectionSentOrNot.postValue(NetworkResult.Error("Network Failure"))
                    else -> _connectionSentOrNot.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later ${t.toString()}"))
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
}