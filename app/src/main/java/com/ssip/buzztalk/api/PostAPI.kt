package com.ssip.buzztalk.api

import com.ssip.buzztalk.models.DefaultJSONResponse
import com.ssip.buzztalk.models.comment.request.CommentRequest
import com.ssip.buzztalk.models.comment.response.AllComments
import com.ssip.buzztalk.models.feed.response.Feed
import com.ssip.buzztalk.models.hashtagfeeds.request.HashTagBody
import com.ssip.buzztalk.models.hashtagfeeds.response.HashTagFeeds
import com.ssip.buzztalk.models.hashtags.request.HashIdBody
import com.ssip.buzztalk.models.hashtags.response.HashTags
import com.ssip.buzztalk.models.likes.request.PostId
import com.ssip.buzztalk.models.likes.response.DefaultJSONBoolResponse
import com.ssip.buzztalk.models.myfeed.response.MyFeed
import com.ssip.buzztalk.models.newfeed.NewFeed
import com.ssip.buzztalk.models.post.request.PostBody
import com.ssip.buzztalk.models.post.response.PostAPIResponse
import com.ssip.buzztalk.models.post.response.singlePost.SinglePost
import com.ssip.buzztalk.models.usernames.UserNames
import com.ssip.buzztalk.utils.Constants
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.w3c.dom.Comment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PostAPI {
    @POST(Constants.POST_CONTENT)
    suspend fun addPost(@Body postBody: PostBody): Response<PostAPIResponse>

    @GET(Constants.GET_USERNAMES)
    suspend fun getUserNames(): Response<UserNames>

    @GET(Constants.GET_FEED)
    suspend fun getFeed(): Response<Feed>

    @GET(Constants.GET_NEW_FEED)
    suspend fun getNewFeed(): Response<NewFeed>

    @POST(Constants.POST_NEW_OPPO_COMPANY)
    suspend fun postNewOpportunitiesCompany(@Body newOpportunities: newOpportunities): Response<DefaultJSONResponse>

    @POST(Constants.POST_NEW_OPPO_UNIVERSITY)
    suspend fun postNewOpportunitiesUniversity(@Body newOpportunities: newOpportunities): Response<DefaultJSONResponse>

    @GET(Constants.GET_MY_FEED)
    suspend fun getMyFeed(): Response<MyFeed>

    @GET(Constants.GET_SINGLE_POST)
    suspend fun getSinglePost(@Path("id") id: String): Response<SinglePost>

    @POST(Constants.COMMENT_ON_POST)
    suspend fun commentOnPost(@Body commentRequest: CommentRequest): Response<DefaultJSONResponse>

    @GET(Constants.GET_ALL_COMMENTS)
    suspend fun getAllComments(@Path("postId") postId: String): Response<AllComments>

    @POST(Constants.GET_HASH_TAG_POSTS)
    suspend fun getHashTagFeeds(@Body hashTagBody: HashTagBody): Response<HashTagFeeds>

    @POST(Constants.GET_HASH_TAG_ID)
    suspend fun getHashTagId(@Body hashIdBody: HashIdBody): Response<HashTags>

    @POST(Constants.LIKE_POST)
    suspend fun likePost(@Body postId: PostId): Response<DefaultJSONResponse>

    @POST(Constants.IS_LIKED)
    suspend fun isPostLiked(@Body postId: PostId): Response<DefaultJSONBoolResponse>

    @Multipart
    @POST (Constants.IMAGE_UPLOAD)
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<DefaultJSONResponse>

    @POST(Constants.GET_IMAGE)
    suspend fun getImage(@Body imageName: ImageName): Response<DefaultJSONResponse>
}

data class newOpportunities(
    val postTitle: String,
    val postContent: String,
    val to: List<String>
)

data class ImageName(
    val imageName: String
)
