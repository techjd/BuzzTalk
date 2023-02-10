package com.ssip.buzztalk.ui.fragments.detailedpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.api.PostAPI
import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.models.comment.request.CommentRequest
import com.ssip.buzztalk.models.comment.response.AllComments
import com.ssip.buzztalk.models.post.response.singlePost.SinglePost
import com.ssip.buzztalk.repository.PostRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailedPostViewModel @Inject constructor(
  private val postRepository: PostRepository
): ViewModel() {

  private val _singlePost: MutableLiveData<NetworkResult<SinglePost>> = MutableLiveData()
  val singlePost: LiveData<NetworkResult<SinglePost>> = _singlePost

  private val _commentResponse: MutableLiveData<NetworkResult<DefaultJSONResponse>> = MutableLiveData()
  val commentSuccess: LiveData<NetworkResult<DefaultJSONResponse>> = _commentResponse

  private val _allComments: MutableLiveData<NetworkResult<AllComments>> = MutableLiveData()
  val allComments: LiveData<NetworkResult<AllComments>> = _allComments

  fun getSinglePost(postId: String) {
    viewModelScope.launch {
      _singlePost.postValue(NetworkResult.Loading())
      _singlePost.postValue(postRepository.getSinglePost(postId))
    }
  }

  fun commentOnPost(commentRequest: CommentRequest) {
    viewModelScope.launch {
      _commentResponse.postValue(NetworkResult.Loading())
      _commentResponse.postValue(postRepository.commentOnPost(commentRequest))
    }
  }

  fun getAllComments(postId: String) {
    viewModelScope.launch {
      _allComments.postValue(NetworkResult.Loading())
      _allComments.postValue(postRepository.getAllComments(postId))
    }
  }

}