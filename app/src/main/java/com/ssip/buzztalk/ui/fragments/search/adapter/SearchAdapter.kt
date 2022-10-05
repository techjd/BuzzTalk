package com.ssip.buzztalk.ui.fragments.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemSearchUserBinding
import com.ssip.buzztalk.models.searchusers.response.User

class SearchAdapter(
    private val glide: RequestManager,
    private val navigate: (id: String) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var users: MutableList<User> = mutableListOf()

    inner class SearchViewHolder(val itemBinding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = users[position]
        with(holder) {
            with(user) {
                itemBinding.userFullName.text = "$firstName $lastName"
                glide.load(R.drawable.user).into(itemBinding.profilePhoto)
                itemBinding.mainLayout.setOnClickListener {
                    navigate(_id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}