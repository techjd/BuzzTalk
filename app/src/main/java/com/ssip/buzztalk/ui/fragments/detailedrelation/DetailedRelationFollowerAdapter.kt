package com.ssip.buzztalk.ui.fragments.detailedrelation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.ItemSearchUserBinding
import com.ssip.buzztalk.models.followers.response.Follower
import com.ssip.buzztalk.models.followers.response.Followers
import com.ssip.buzztalk.models.searchusers.response.User

class DetailedRelationFollowerAdapter(
    private val glide: RequestManager,
    private val navigate: (id: String) -> Unit
) : RecyclerView.Adapter<DetailedRelationFollowerAdapter.DetailedRelationFollowerFollowingViewHolder>() {

    var users: MutableList<Follower> = mutableListOf()

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

                itemBinding.userFullName.text = "${followerId.firstName} ${followerId.lastName}"
                glide.load(R.drawable.user).into(itemBinding.profilePhoto)
                itemBinding.mainLayout.setOnClickListener {
                    navigate(followerId._id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}