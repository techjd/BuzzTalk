package com.ssip.buzztalk.ui.fragments.chat.messages

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ssip.buzztalk.databinding.ItemChatMessageMyselfBinding
import com.ssip.buzztalk.databinding.ItemChatMessageOtherBinding

sealed class MessageRecyclerViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    class View1ViewHolder(val itemChatMessageMyselfBinding: ItemChatMessageMyselfBinding) : MessageRecyclerViewHolder(itemChatMessageMyselfBinding) {
        fun bind(message: String){
            itemChatMessageMyselfBinding.ourMessage.text = message
        }
    }

    class View2ViewHolder(val itemChatMessageOtherBinding: ItemChatMessageOtherBinding) : MessageRecyclerViewHolder(itemChatMessageOtherBinding) {
        fun bind(message: String){
            itemChatMessageOtherBinding.otherMessage.text = message
        }
    }
}
