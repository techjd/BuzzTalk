package com.ssip.buzztalk.ui.fragments.chat.mygroup

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ssip.buzztalk.databinding.ItemChatMessageMyselfBinding
import com.ssip.buzztalk.databinding.ItemChatMessageOtherBinding
import com.ssip.buzztalk.databinding.ItemGroupChatMessageMyselfBinding
import com.ssip.buzztalk.databinding.ItemGroupChatMessageOtherBinding
import com.ssip.buzztalk.ui.fragments.chat.messages.MessageRecyclerViewHolder

sealed class GroupMessageRecyclerViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
  class View1ViewHolder(private val itemGroupChatMessageMyselfBinding: ItemGroupChatMessageMyselfBinding) : GroupMessageRecyclerViewHolder(itemGroupChatMessageMyselfBinding) {
    fun bind(userName: String, message: String){
      itemGroupChatMessageMyselfBinding.fullName.text = userName
      itemGroupChatMessageMyselfBinding.ourMessage.text = message
    }
  }

  class View2ViewHolder(private val itemGroupChatMessageOtherBinding: ItemGroupChatMessageOtherBinding) : GroupMessageRecyclerViewHolder(itemGroupChatMessageOtherBinding) {
    fun bind(userName:String, message: String){
      itemGroupChatMessageOtherBinding.fullName.text = userName
      itemGroupChatMessageOtherBinding.otherMessage.text = message
    }
  }
}
