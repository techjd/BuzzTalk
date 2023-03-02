package com.ssip.buzztalk.ui.fragments.newgroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemSearchUserBinding
import com.ssip.buzztalk.databinding.ItemSelectedMemberBinding
import com.ssip.buzztalk.models.connections.response.allConnections.Connection
import com.ssip.buzztalk.ui.fragments.newgroup.ConversationsConnectionsAdapter.ConversationsConnectionsViewHolder

class SelectedMembersAdapter(
  private val glide: RequestManager,
  private val userId: String,
  private val onClick: (connection: Connection) -> Unit
) : RecyclerView.Adapter<SelectedMembersAdapter.SelectedMembersViewHolder>() {

  var users: MutableList<Connection> = mutableListOf()

  inner class SelectedMembersViewHolder(val itemBinding: ItemSelectedMemberBinding) :
    RecyclerView.ViewHolder(itemBinding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMembersViewHolder {
    val binding =
      ItemSelectedMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return SelectedMembersViewHolder(binding)
  }

  override fun onBindViewHolder(holder: SelectedMembersViewHolder, position: Int) {
    val user = users[position]
    with(holder) {
      with(user) {
        if (from._id == userId) {
          itemBinding.selectedMemberName.text = "${to.firstName} ${to.lastName}"
          glide.load(R.drawable.user).into(itemBinding.cirularImageProfile)
          itemBinding.remove.setOnClickListener {
            onClick(user)
          }
        } else if(to._id == userId) {
          itemBinding.selectedMemberName.text = "${from.firstName} ${from.lastName}"
          glide.load(R.drawable.user).into(itemBinding.cirularImageProfile)
          itemBinding.remove.setOnClickListener {
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