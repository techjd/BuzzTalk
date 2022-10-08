package com.ssip.buzztalk.ui.fragments.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.feed.response.Feed
import com.ssip.buzztalk.models.newfeed.NewFeed
import com.ssip.buzztalk.models.post.request.PostBody
import com.ssip.buzztalk.models.post.response.PostAPIResponse
import com.ssip.buzztalk.models.usernames.UserNames
import com.ssip.buzztalk.repository.PostRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val postRepository: PostRepository): ViewModel() {

    private val _addPost: MutableLiveData<NetworkResult<PostAPIResponse>> = MutableLiveData()
    val addPost: LiveData<NetworkResult<PostAPIResponse>> = _addPost

    private val _usernames: MutableLiveData<NetworkResult<UserNames>> = MutableLiveData()
    val usernames: LiveData<NetworkResult<UserNames>> = _usernames

    private val _feed: MutableLiveData<NetworkResult<Feed>> = MutableLiveData()
    val feed: LiveData<NetworkResult<Feed>> = _feed

    private val _newFeed: MutableLiveData<NetworkResult<NewFeed>> = MutableLiveData()
    val newFeed: LiveData<NetworkResult<NewFeed>> = _newFeed

    fun addPost(postBody: PostBody) {
        viewModelScope.launch {
            _addPost.postValue(NetworkResult.Loading())
            _addPost.postValue(postRepository.addPost(postBody))
        }
    }

    fun getUserNames() {
        viewModelScope.launch {
            _usernames.postValue(NetworkResult.Loading())
            _usernames.postValue(postRepository.getUserNames())
        }
    }

    fun getFeed() {
        viewModelScope.launch {
            _feed.postValue(NetworkResult.Loading())
            _feed.postValue(postRepository.getFeed())
        }
    }

    fun getNewFeed() {
        viewModelScope.launch {
            _newFeed.postValue(NetworkResult.Loading())
            _newFeed.postValue(postRepository.getNewfeed())
        }
    }
}