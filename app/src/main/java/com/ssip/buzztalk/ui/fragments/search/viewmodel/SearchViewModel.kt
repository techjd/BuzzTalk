package com.ssip.buzztalk.ui.fragments.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.searchusers.response.SearchUsers
import com.ssip.buzztalk.repository.UserRepository
import com.ssip.buzztalk.utils.ErrorResponse
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val userRepository: UserRepository,
    private val errorResponse: ErrorResponse
): ViewModel() {
    private val _allSearchUsers: MutableLiveData<NetworkResult<SearchUsers>> = MutableLiveData()
    val allSearchUsers: LiveData<NetworkResult<SearchUsers>> = _allSearchUsers

    fun getAllSearchUsers() {
        viewModelScope.launch {
            try {
                if (networkManager.hasInternetConnection()) {
                    val data = userRepository.getAllSearchUsers()
                    if (data.isSuccessful) {
                        _allSearchUsers.postValue(NetworkResult.Success(data.body()!!))
                    } else {
                        Log.d("ERROR", "getUserInfo: Some Error Occurred")
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        _allSearchUsers.postValue(NetworkResult.Error(error.message))
                    }
                } else {
                    _allSearchUsers.postValue(NetworkResult.Error("No Internet Connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _allSearchUsers.postValue(NetworkResult.Error("Network Failure"))
                    else -> _allSearchUsers.postValue(NetworkResult.Error("Some Error Occurred , Please Try Again Later"))
                }
            }
        }
    }
}