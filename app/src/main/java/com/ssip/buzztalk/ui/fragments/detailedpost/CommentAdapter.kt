package com.ssip.buzztalk.ui.fragments.detailedpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemCommentBinding

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

  var comments: MutableList<com.ssip.buzztalk.models.comment.response.Data> = mutableListOf()

  inner class CommentViewHolder(val itemCommentBinding: ItemCommentBinding):
    RecyclerView.ViewHolder(itemCommentBinding.root)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CommentViewHolder {
    val binding =
      ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return CommentViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return comments.size
  }

  override fun onBindViewHolder(
    holder: CommentViewHolder,
    position: Int
  ) {
    val comment = comments[position]

    with(holder.itemCommentBinding) {
      val fullName = "${comment.userId.firstName} ${comment.userId.lastName}"
      val userBio = "${comment.userId.userType}"
      val commentContent = "${comment.content}"

      userName.text = fullName
      bio.text = userBio
      content.text = commentContent
    }
  }
}