package com.ssip.buzztalk.ui.fragments.connections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemConnectionRequestBinding
import com.ssip.buzztalk.models.connections.response.connectionrequests.Request

class ConnectionRequestsAdapter(
    private val glide: RequestManager,
    private val accept: (id: String) -> Unit,
    private val reject: (id: String) -> Unit
) : RecyclerView.Adapter<ConnectionRequestsAdapter.ConnectionViewHolder>() {

    var requests: MutableList<Request> = mutableListOf()

    inner class ConnectionViewHolder(val itemBinding: ItemConnectionRequestBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder {
        val binding =
            ItemConnectionRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConnectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConnectionViewHolder, position: Int) {
        val user = requests[position]
        with(holder) {
            with(user) {
                itemBinding.userFullName.text = "${user.from.firstName} ${user.from.lastName}"
                glide.load(R.drawable.user).into(itemBinding.staticConnectionRequestImage)
                itemBinding.accept.setOnClickListener {
                    accept(_id)
                }
                itemBinding.reject.setOnClickListener {
                    reject(_id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return requests.size
    }
}