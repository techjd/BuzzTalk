package com.ssip.buzztalk.ui.fragments.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssip.buzztalk.api.ImageName
import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.models.feed.response.Feed
import com.ssip.buzztalk.models.likes.request.PostId
import com.ssip.buzztalk.models.newfeed.NewFeed
import com.ssip.buzztalk.models.post.request.PostBody
import com.ssip.buzztalk.models.post.response.PostAPIResponse
import com.ssip.buzztalk.models.usernames.UserNames
import com.ssip.buzztalk.repository.PostRepository
import com.ssip.buzztalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import okhttp3.MultipartBody

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

    private val _likePost: MutableLiveData<NetworkResult<DefaultJSONResponse>> = MutableLiveData()
    val likePost: LiveData<NetworkResult<DefaultJSONResponse>> = _likePost

    private val _imageUpload: MutableLiveData<NetworkResult<DefaultJSONResponse>> = MutableLiveData()
    val imageUpload: LiveData< NetworkResult<DefaultJSONResponse>> = _imageUpload

    private val _imageUrl: MutableLiveData<NetworkResult<DefaultJSONResponse>> = MutableLiveData()
    val imageUrl: LiveData<NetworkResult<DefaultJSONResponse>> = _imageUrl

    private val _filteredFeed: MutableLiveData<Feed> = MutableLiveData()
    val filteredFeed: LiveData<Feed> = _filteredFeed

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
            val data = postRepository.getFeed()
            if (data.data == null) {
                _feed.postValue(NetworkResult.Error("Some Error Happened"))
            } else {
                data.data.let { feed ->
                    for(feeds in feed.data?.feed!!) {
                        feeds.isLiked = postRepository.isPostLiked(PostId(feeds.postId._id)).data!!.data
                    }
                }
                _feed.postValue(NetworkResult.Success(data.data))
            }
        }
    }

    fun likePost(postId: PostId) {
        viewModelScope.launch {
            _likePost.postValue(NetworkResult.Loading())
            _likePost.postValue(postRepository.likePost(postId))
        }
    }

    fun uploadImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            _imageUpload.postValue(NetworkResult.Loading())
            _imageUpload.postValue(postRepository.uploadImage(image))
        }
    }

    fun getImageUrl(imageName: String) {
        viewModelScope.launch {
            _imageUrl.postValue(NetworkResult.Loading())
            _imageUrl.postValue(postRepository.getImage(ImageName(imageName)))
        }
    }

    fun getNewFeed() {
        viewModelScope.launch {
            _newFeed.postValue(NetworkResult.Loading())
            _newFeed.postValue(postRepository.getNewfeed())
        }
    }

    fun filterFeed(choices: List<String>) {
        Log.d("CHOICES ", "filterFeed: ${choices}")
        viewModelScope.launch {
            if(choices.isEmpty()) {
                getFeed()
            } else {
                val list = _feed.value?.data?.data?.feed!!

                for(items in list) {
                    val set = items.postsFor.toSet()
                    for(choice in choices) {
                        if (set.contains(choice)) {
                            if (_filteredFeed.value?.data?.feed?.isNotEmpty() == true) {
                                val lastItem = _filteredFeed.value?.data!!.feed.last()
                                if (items._id != lastItem._id) {
                                    _filteredFeed.value?.data?.feed?.add(items)
                                }
                            } else {
                                _filteredFeed.value?.data?.feed?.add(items)
                            }
                        }
                    }
                }
                Log.d("ALUE ", "filterFeed: ${_filteredFeed.value?.data?.feed}")
            }
        }
    }
}