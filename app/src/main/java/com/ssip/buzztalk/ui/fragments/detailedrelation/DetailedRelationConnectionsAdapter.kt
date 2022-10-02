package com.ssip.buzztalk.ui.fragments.detailedrelation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemSearchUserBinding
import com.ssip.buzztalk.models.connections.response.allConnections.Connection
import com.ssip.buzztalk.models.searchusers.response.User

class DetailedRelationConnectionsAdapter(
    private val glide: RequestManager,
    private val userId: String,
    private val navigate: (id: String) -> Unit
) : RecyclerView.Adapter<DetailedRelationConnectionsAdapter.DetailedRelationConnectionsViewHolder>() {

    var users: MutableList<Connection> = mutableListOf()

    inner class DetailedRelationConnectionsViewHolder(val itemBinding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedRelationConnectionsViewHolder {
        val binding =
            ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailedRelationConnectionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailedRelationConnectionsViewHolder, position: Int) {
        val user = users[position]
        with(holder) {
            with(user) {
                if (from._id == userId) {
                    itemBinding.userFullName.text = "${to.firstName} ${to.lastName}"
                    glide.load(R.drawable.user).into(itemBinding.profilePhoto)
                    itemBinding.mainLayout.setOnClickListener {
                        navigate(to._id)
                    }
                } else if(to._id == userId) {
                    itemBinding.userFullName.text = "${from.firstName} ${from.lastName}"
                    glide.load(R.drawable.user).into(itemBinding.profilePhoto)
                    itemBinding.mainLayout.setOnClickListener {
                        navigate(from._id)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}