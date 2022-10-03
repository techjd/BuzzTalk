package com.ssip.buzztalk.ui.fragments.chat.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemConversationBinding
import com.ssip.buzztalk.databinding.ItemSearchUserBinding
import com.ssip.buzztalk.models.chat.response.conversations.Conversation
import com.ssip.buzztalk.models.chat.response.conversations.Conversations
import com.ssip.buzztalk.models.searchusers.response.User
import com.ssip.buzztalk.ui.fragments.search.adapter.SearchAdapter

class ConversationsAdapter(
    private val glide: RequestManager,
    private val myUserId: String,
    private val navigate : (toId: String) -> Unit
) :
    RecyclerView.Adapter<ConversationsAdapter.ConversationsViewHolder>() {

    var conversations: MutableList<Conversation> = mutableListOf()

    inner class ConversationsViewHolder(val itemBinding: ItemConversationBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsAdapter.ConversationsViewHolder {
        val binding =
            ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationsViewHolder, position: Int) {
        val conversation = conversations[position]
        with(holder) {
            with(conversation) {
                val toId = conversations[position].recipientObj
                var userName: String? = null
                var idToSend: String? = null
                for (i in 0..toId.size) {
                    if (toId[i]._id != myUserId) {
                        idToSend = toId[i]._id
                        userName = "${toId[i].firstName}  ${toId[i].lastName}"
                        break
                    }
                }
                glide.load(R.drawable.user).into(itemBinding.profilePhoto)
                itemBinding.userFullName.text = userName
                itemBinding.lastMessage.text = lastMessage
                itemBinding.mainLayout.setOnClickListener {
                    navigate(idToSend!!)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return conversations.size
    }
}