package com.ssip.buzztalk.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.api.newOpportunities
import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.repository.PostRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostOppoViewModel @Inject constructor(private val postRepository: PostRepository): ViewModel() {

    private val _postOppoCompany: MutableLiveData<NetworkResult<DefaultJSONResponse>> = MutableLiveData()
    val postOppoCompany:LiveData<NetworkResult<DefaultJSONResponse>> = _postOppoCompany

    private val _postOppoUniversity: MutableLiveData<NetworkResult<DefaultJSONResponse>> = MutableLiveData()
    val postOppoUniversity:LiveData<NetworkResult<DefaultJSONResponse>> = _postOppoCompany

    fun postOppoCompany(newOpportunities: newOpportunities) {
        viewModelScope.launch {
            _postOppoCompany.postValue(NetworkResult.Loading())
            _postOppoCompany.postValue(postRepository.postNewOppoCompany(newOpportunities))
        }
    }

    fun postOppoUniversitt(newOpportunities: newOpportunities) {
        viewModelScope.launch {
            _postOppoUniversity.postValue(NetworkResult.Loading())
            _postOppoUniversity.postValue(postRepository.postNewOppoUniversity(newOpportunities))
        }
    }

}