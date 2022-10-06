package com.ssip.buzztalk.ui.fragments.detailedrelation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.connections.response.allConnections.AllConnections
import com.ssip.buzztalk.models.followers.request.FolloweeId
import com.ssip.buzztalk.models.followers.response.Followers
import com.ssip.buzztalk.models.following.request.FollowerId
import com.ssip.buzztalk.models.following.response.Followings
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.models.totalCount.response.FollowersFollowingCount
import com.ssip.buzztalk.repository.UserRepository
import com.ssip.buzztalk.utils.ErrorResponse
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class DetailedRelationViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val userRepository: UserRepository,
    private val errorResponse: ErrorResponse
): ViewModel() {

    private val _followersFollowing: MutableLiveData<NetworkResult<FollowersFollowingCount>> = MutableLiveData()
    val followersFollowingCount: LiveData<NetworkResult<FollowersFollowingCount>> = _followersFollowing

    private val _allConnections: MutableLiveData<NetworkResult<AllConnections>> = MutableLiveData()
    val allConnections: LiveData<NetworkResult<AllConnections>> = _allConnections

    private val _allFollowers: MutableLiveData<NetworkResult<Followers>> = MutableLiveData()
    val allFollowers: LiveData<NetworkResult<Followers>> = _allFollowers

    private val _allFollowing: MutableLiveData<NetworkResult<Followings>> = MutableLiveData()
    val allFollowing: LiveData<NetworkResult<Followings>> = _allFollowing

    fun getFollowersFollowingCount(token: String, userID: UserID) {
        viewModelScope.launch {
            _followersFollowing.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.getTotalFollowersFollowing(token, userID)
                    if (data.isSuccessful) {
                        _followersFollowing.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred ${data.errorBody()}")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _followersFollowing.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _followersFollowing.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _followersFollowing.postValue(NetworkResult.Error("Network Failure"))
                    else -> _followersFollowing.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
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
                        Log.d("CONNECTIONS ", "getAllConnections: ${data.body()!!.data.connections}")
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
                    else -> _allConnections.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }

    fun getAllFollowers(token: String, followeeId: FolloweeId) {
        viewModelScope.launch {
                _allFollowers.postValue(NetworkResult.Loading())
                try {
                    if (networkManager.hasInternetConnection()) {
                        val data = userRepository.getAllFollowers(token, followeeId)
                        if (data.isSuccessful) {
                            _allFollowers.postValue(NetworkResult.Success(data.body()!!))
                            Log.d("CONNECTIONS ", "getAllConnections: ${data.body()!!.data.followers}")
                        } else {
                            Log.d("ERROR", "getUserInfo: Some Error Occurred ${data.errorBody()}")
                            val error = errorResponse.giveErrorResult(data.errorBody()!!)
                            _allFollowers.postValue(NetworkResult.Error(error.message))
                        }
                    } else {
                        _allFollowers.postValue(NetworkResult.Error("No Internet Connection"))
                    }
                } catch (t: Throwable) {
                    when (t) {
                        is IOException -> _allFollowers.postValue(NetworkResult.Error("Network Failure"))
                        else -> _allFollowers.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                    }
                }
        }
    }

    fun getAllFollowing(token: String, followerId: FollowerId) {
        viewModelScope.launch {
            _allFollowing.postValue(NetworkResult.Loading())
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.getAllFollowing(token, followerId)
                    if (data.isSuccessful) {
                        _allFollowing.postValue(NetworkResult.Success(data.body()!!))
                        Log.d("CONNECTIONS ", "getAllConnections: ${data.body()!!.data.following}")
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred ${errorResponse.giveErrorResult(data.errorBody()!!)}")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _allFollowing.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _allFollowing.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _allFollowing.postValue(NetworkResult.Error("Network Failure"))
                    else -> _allFollowing.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }
}