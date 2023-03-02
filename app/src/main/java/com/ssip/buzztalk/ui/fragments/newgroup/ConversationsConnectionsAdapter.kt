package com.ssip.buzztalk.ui.fragments.newgroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemSearchUserBinding
import com.ssip.buzztalk.models.connections.response.allConnections.Connection
import com.ssip.buzztalk.ui.fragments.detailedrelation.DetailedRelationConnectionsAdapter
import com.ssip.buzztalk.ui.fragments.detailedrelation.DetailedRelationConnectionsAdapter.DetailedRelationConnectionsViewHolder

class ConversationsConnectionsAdapter(
  private val glide: RequestManager,
  private val userId: String,
  private val onClick: (connection: Connection) -> Unit
) : RecyclerView.Adapter<ConversationsConnectionsAdapter.ConversationsConnectionsViewHolder>() {

  var users: MutableList<Connection> = mutableListOf()

  inner class ConversationsConnectionsViewHolder(val itemBinding: ItemSearchUserBinding) :
    RecyclerView.ViewHolder(itemBinding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsConnectionsViewHolder {
    val binding =
      ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ConversationsConnectionsViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ConversationsConnectionsViewHolder, position: Int) {
    val user = users[position]
    with(holder) {
      with(user) {
        if (from._id == userId) {
          itemBinding.userFullName.text = "${to.firstName} ${to.lastName}"
          glide.load(R.drawable.user).into(itemBinding.profilePhoto)
          itemBinding.mainLayout.setOnClickListener {
            onClick(user)
          }
        } else if(to._id == userId) {
          itemBinding.userFullName.text = "${from.firstName} ${from.lastName}"
          glide.load(R.drawable.user).into(itemBinding.profilePhoto)
          itemBinding.mainLayout.setOnClickListener {
            onClick(user)
          }
        }
      }
    }
  }

  override fun getItemCount(): Int {
    return users.size
  }
}