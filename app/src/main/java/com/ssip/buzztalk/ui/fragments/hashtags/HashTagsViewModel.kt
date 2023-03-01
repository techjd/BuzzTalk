package com.ssip.buzztalk.ui.fragments.hashtags

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.models.hashtagfeeds.request.HashTagBody
import com.ssip.buzztalk.models.hashtagfeeds.response.HashTagFeeds
import com.ssip.buzztalk.models.hashtags.request.HashIdBody
import com.ssip.buzztalk.models.hashtags.response.HashTags
import com.ssip.buzztalk.repository.PostRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HashTagsViewModel @Inject constructor(
  private val postRepository: PostRepository
): ViewModel() {

  private val _hashTagsPosts: MutableLiveData<NetworkResult<HashTagFeeds>> = MutableLiveData()
  val hashTagsPosts: LiveData<NetworkResult<HashTagFeeds>> = _hashTagsPosts

  private val _hashId: MutableLiveData<NetworkResult<HashTags>> = MutableLiveData()
  val hashId: LiveData<NetworkResult<HashTags>> = _hashId

  fun getHashTagPosts(hashTagBody: HashTagBody) {
    viewModelScope.launch {
      _hashTagsPosts.postValue(NetworkResult.Loading())
      _hashTagsPosts.postValue(postRepository.getHashTagFeeds(hashTagBody))
    }
  }

  fun getHashId(hashIdBody: HashIdBody) {
    Log.d("STR ", "getHashId: ${hashIdBody.hashTag}")
    viewModelScope.launch {
      _hashId.postValue(NetworkResult.Loading())
      _hashId.postValue(postRepository.getHashTagId(hashIdBody))
    }
  }
}