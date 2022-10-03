package com.ssip.buzztalk.ui.fragments.chat.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemChatMessageMyselfBinding
import com.ssip.buzztalk.databinding.ItemChatMessageOtherBinding
import com.ssip.buzztalk.models.chat.MessageModel
import com.ssip.buzztalk.models.chat.response.messages.Message


class MessageAdapter(val myUserId: String) : RecyclerView.Adapter<MessageRecyclerViewHolder>() {

    var messages: MutableList<MessageModel> = mutableListOf()

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRecyclerViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return MessageRecyclerViewHolder.View1ViewHolder(
                ItemChatMessageMyselfBinding.inflate(
                    LayoutInflater.from(parent.context
                    ), parent, false
                )
            )
        }
        return MessageRecyclerViewHolder.View2ViewHolder(
            ItemChatMessageOtherBinding.inflate(
                LayoutInflater.from(parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageRecyclerViewHolder, position: Int) {
        val message = messages[position]
        when(holder) {
            is MessageRecyclerViewHolder.View1ViewHolder -> {
                holder.bind(message.body)
            }
            is MessageRecyclerViewHolder.View2ViewHolder -> {
                holder.bind(message.body)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addNewMessage(messageModel: MessageModel) {
        messages.add(messageModel)
        notifyItemInserted(messages.size - 1)
    }
//    fun addItem(messagesItem: MessagesItem) {
//        messages.add(messagesItem)
//        Log.d("LAST ITEM", "Last Item ${messages[messages.size - 1]}")
//        Log.d("SIZE", "addItem: ${messages.size}")
//    }

    fun returnLastPosition(): Int {
        return messages.size - 1
    }


    override fun getItemViewType(position: Int): Int {
        if (messages[position].from == myUserId) {
            return VIEW_TYPE_ONE
        }
        return VIEW_TYPE_TWO
    }
}