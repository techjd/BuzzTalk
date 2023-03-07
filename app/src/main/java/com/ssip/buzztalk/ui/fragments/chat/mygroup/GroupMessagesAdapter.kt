package com.ssip.buzztalk.ui.fragments.chat.mygroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemChatMessageMyselfBinding
import com.ssip.buzztalk.databinding.ItemChatMessageOtherBinding
import com.ssip.buzztalk.databinding.ItemGroupChatMessageMyselfBinding
import com.ssip.buzztalk.databinding.ItemGroupChatMessageOtherBinding
import com.ssip.buzztalk.models.chat.MessageModel
import com.ssip.buzztalk.models.groupchat.request.GroupMessageBody
import com.ssip.buzztalk.ui.fragments.chat.messages.MessageRecyclerViewHolder

class GroupMessagesAdapter(
  val myUserId: String
  ) : RecyclerView.Adapter<GroupMessageRecyclerViewHolder>() {

  var messages: MutableList<GroupMessageBody> = mutableListOf()

  companion object {
    const val VIEW_TYPE_ONE = 1
    const val VIEW_TYPE_TWO = 2
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMessageRecyclerViewHolder {
    if (viewType == VIEW_TYPE_ONE) {
      return GroupMessageRecyclerViewHolder.View1ViewHolder(
        ItemGroupChatMessageMyselfBinding.inflate(
          LayoutInflater.from(parent.context
          ), parent, false
        )
      )
    }
    return GroupMessageRecyclerViewHolder.View2ViewHolder(
      ItemGroupChatMessageOtherBinding.inflate(
        LayoutInflater.from(parent.context
        ), parent, false
      )
    )
  }

  override fun onBindViewHolder(holder: GroupMessageRecyclerViewHolder, position: Int) {
    val message = messages[position]
    when(holder) {
      is GroupMessageRecyclerViewHolder.View1ViewHolder -> {
        holder.bind(message.fromName , message.messageContent)
      }
      is GroupMessageRecyclerViewHolder.View2ViewHolder -> {
        holder.bind(message.fromName , message.messageContent)
      }
    }
  }

  override fun getItemCount(): Int {
    return messages.size
  }

  fun addNewMessage(groupMessageBody: GroupMessageBody) {
    messages.add(groupMessageBody)
    notifyItemInserted(messages.size - 1)
  }

  fun returnLastPosition(): Int {
    return messages.size - 1
  }

  override fun getItemViewType(position: Int): Int {
    if (messages[position].fromId == myUserId) {
      return VIEW_TYPE_ONE
    }
    return VIEW_TYPE_TWO
  }
}