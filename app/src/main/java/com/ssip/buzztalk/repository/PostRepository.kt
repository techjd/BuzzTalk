package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.ChatAPI
import com.ssip.buzztalk.api.PostAPI
import com.ssip.buzztalk.api.newOpportunities
import com.ssip.buzztalk.models.comment.request.CommentRequest
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

    suspend fun postNewOppoCompany(newOpportunities: newOpportunities) = safeApiCall {
        postAPI.postNewOpportunitiesCompany(newOpportunities)
    }

    suspend fun postNewOppoUniversity(newOpportunities: newOpportunities) = safeApiCall {
        postAPI.postNewOpportunitiesUniversity(newOpportunities)
    }

    suspend fun getMyFeed() = safeApiCall {
        postAPI.getMyFeed()
    }

    suspend fun getSinglePost(id: String) = safeApiCall {
        postAPI.getSinglePost(id)
    }

    suspend fun commentOnPost(commentRequest: CommentRequest) = safeApiCall {
        postAPI.commentOnPost(commentRequest)
    }

    suspend fun getAllComments(postId: String) = safeApiCall {
        postAPI.getAllComments(postId)
    }
}