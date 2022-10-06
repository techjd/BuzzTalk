package com.ssip.buzztalk.ui.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.fobid.linkabletext.view.OnLinkClickListener
import com.ssip.buzztalk.databinding.ItemConnectionRequestBinding
import com.ssip.buzztalk.databinding.ItemPostBinding
import com.ssip.buzztalk.models.connections.response.connectionrequests.Request
import com.ssip.buzztalk.models.feed.response.Feed
import com.ssip.buzztalk.models.feed.response.FeedX

class PostsAdapter(
    val onClickListener: (String) -> Unit,
    val showToast: (String) -> Unit
) :
    RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    var posts: MutableList<FeedX> = mutableListOf()

    inner class PostsViewHolder(val itemPostBinding: ItemPostBinding) : RecyclerView.ViewHolder(itemPostBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val singlePost = posts[position]

        with(holder) {
            itemPostBinding.userName.text = "${singlePost.postId.userId.firstName} ${singlePost.postId.userId.lastName}"
//            itemPostBinding.content.setLinkHint(singlePost.postId.content)

            itemPostBinding.content.apply {
                setText(singlePost.postId.content)
                setOnLinkClickListener(object : OnLinkClickListener {
                    override fun onEmailAddressClick(email: String) {

                    }

                    override fun onHashtagClick(hashtag: String) {
                        showToast(hashtag)
                    }

                    override fun onMentionClick(mention: String) {
                        showToast(mention)
                    }

                    override fun onPhoneClick(phone: String) {

                    }

                    override fun onWebUrlClick(url: String) {

                    }

                })
            }

//            itemPostBinding.content.text = "${singlePost.postId.content}"
            itemPostBinding.totalLikesCnt.text = singlePost.postId.likes.toString()
            itemPostBinding.totalComments.text = "${singlePost.postId.comments.toString()} comments"
//            itemPostBinding.root.setOnClickListener {
//                onClickListener(singlePost.postId._id)
//            }
//
//            itemPostBinding.content.setOnLinkClickListener { linkType, matchedText ->
//                showToast(matchedText)
//            }

        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}