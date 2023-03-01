package com.ssip.buzztalk.repository

import com.ssip.buzztalk.api.ChatAPI
import com.ssip.buzztalk.api.ImageName
import com.ssip.buzztalk.api.PostAPI
import com.ssip.buzztalk.api.newOpportunities
import com.ssip.buzztalk.models.comment.request.CommentRequest
import com.ssip.buzztalk.models.hashtagfeeds.request.HashTagBody
import com.ssip.buzztalk.models.hashtags.request.HashIdBody
import com.ssip.buzztalk.models.likes.request.PostId
import com.ssip.buzztalk.models.post.request.PostBody
import javax.inject.Inject
import okhttp3.MultipartBody

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

    suspend fun getHashTagFeeds(hashTagBody: HashTagBody) = safeApiCall {
        postAPI.getHashTagFeeds(hashTagBody)
    }

    suspend fun getHashTagId(hashIdBody: HashIdBody) = safeApiCall {
        postAPI.getHashTagId(hashIdBody)
    }

    suspend fun likePost(postId: PostId) = safeApiCall {
        postAPI.likePost(postId)
    }

    suspend fun isPostLiked(postId: PostId) = safeApiCall {
        postAPI.isPostLiked(postId)
    }

    suspend fun uploadImage(image: MultipartBody.Part) = safeApiCall {
        postAPI.uploadImage(image)
    }

    suspend fun getImage(imageName: ImageName) = safeApiCall {
        postAPI.getImage(imageName)
    }
}