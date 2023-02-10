package com.ssip.buzztalk.ui.fragments.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemMyPostsBinding

class ProfileFeedAdapter(
    private val onPostOnClick: (postId: String) -> Unit
): RecyclerView.Adapter<ProfileFeedAdapter.ProfileFeedViewHolder>() {

  var posts: MutableList<com.ssip.buzztalk.models.myfeed.response.Feed> = mutableListOf()

  inner class ProfileFeedViewHolder(val myPostsBinding: ItemMyPostsBinding) : RecyclerView.ViewHolder(myPostsBinding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ProfileFeedViewHolder {
    val binding =
      ItemMyPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ProfileFeedViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return posts.size
  }

  override fun onBindViewHolder(
    holder: ProfileFeedViewHolder,
    position: Int
  ) {
    val post = posts[position]
    with(holder.myPostsBinding) {
      userName.text = "${post.userId.firstName} ${post.userId.lastName}"
      content.text = post.content
      textView6.text = post.userId.userType
    }
  }
}