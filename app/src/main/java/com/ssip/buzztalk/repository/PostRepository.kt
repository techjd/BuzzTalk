package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.ChatAPI
import com.ssip.buzztalk.api.PostAPI
import com.ssip.buzztalk.models.post.request.PostBody
import javax.inject.Inject

class PostRepository @Inject constructor(private val postAPI: PostAPI): BaseRepository() {

    suspend fun addPost(postBody: PostBody) = safeApiCall {
        postAPI.addPost(postBody)
    }

    suspend fun getUserNames() = safeApiCall {
        postAPI.getUserNames()
    }

    suspend fun getFeed() = safeApiCall {
        postAPI.getFeed()
    }

    suspend fun getNewfeed() = safeApiCall {
        postAPI.getNewFeed()
    }
}