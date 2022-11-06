package com.ssip.buzztalk.ui.fragments.detailedrelation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemSearchUserBinding
import com.ssip.buzztalk.models.searchusers.response.User

class DetailedRelationFollowerFollowingAdapter(
    private val glide: RequestManager,
    private val navigate: (id: String) -> Unit
) : RecyclerView.Adapter<DetailedRelationFollowerFollowingAdapter.DetailedRelationFollowerFollowingViewHolder>() {

    var users: MutableList<User> = mutableListOf()

    inner class DetailedRelationFollowerFollowingViewHolder(val itemBinding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedRelationFollowerFollowingViewHolder {
        val binding =
            ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailedRelationFollowerFollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailedRelationFollowerFollowingViewHolder, position: Int) {
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