package com.ssip.buzztalk.ui.fragments.chat.mygroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemConversationBinding
import com.ssip.buzztalk.models.chat.response.conversations.Conversation
import com.ssip.buzztalk.models.groupchat.response.AllGroup
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationsAdapter
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationsAdapter.ConversationsViewHolder

class MyGroupsAdapter(
  private val glide: RequestManager,
  private val navigate : (toId: String) -> Unit
) :
  RecyclerView.Adapter<MyGroupsAdapter.MyGroupsViewHolder>() {

  var groups: MutableList<AllGroup> = mutableListOf()

  inner class MyGroupsViewHolder(val itemBinding: ItemConversationBinding) :
    RecyclerView.ViewHolder(itemBinding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupsAdapter.MyGroupsViewHolder {
    val binding =
      ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MyGroupsViewHolder(binding)
  }

  override fun onBindViewHolder(holder: MyGroupsViewHolder, position: Int) {
    val group = groups[position]
    with(holder) {
      with(group) {
        val toGroupId = this.groupId._id

        glide.load(R.drawable.user).into(itemBinding.profilePhoto)
        itemBinding.userFullName.text = this.groupId.groupName
        itemBinding.lastMessage.text = if (groupId.lastMessage.isNullOrEmpty()) "No Messages Sent" else groupId.lastMessage
        itemBinding.mainLayout.setOnClickListener {
          navigate(toGroupId)
        }
      }
    }
  }

  override fun getItemCount(): Int {
    return groups.size
  }
}