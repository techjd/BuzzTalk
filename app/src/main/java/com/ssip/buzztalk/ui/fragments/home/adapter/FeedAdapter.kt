package com.ssip.buzztalk.ui.fragments.home.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemNewFeedBinding
import com.ssip.buzztalk.databinding.ItemPostBinding
import com.ssip.buzztalk.models.feed.response.FeedX

class FeedAdapter(
    val onClickListener: (String) -> Unit,
    val showToast: (String) -> Unit
) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    var posts: MutableList<com.ssip.buzztalk.models.newfeed.Feed> = mutableListOf()

    inner class FeedViewHolder(val itemNewFeedBinding: ItemNewFeedBinding) : RecyclerView.ViewHolder(itemNewFeedBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding =
            ItemNewFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val singlePost = posts[position]

        with(holder) {
            if (singlePost.postedByCompany == null) {
                itemNewFeedBinding.userName.text = "${singlePost.postedByUniversity.universityName}"
                itemNewFeedBinding.content.text = singlePost.feedContent
                itemNewFeedBinding.title.text = singlePost.feedTitle
                itemNewFeedBinding.bio.visibility = View.INVISIBLE
            } else {
                itemNewFeedBinding.userName.text = "${singlePost.postedByCompany.companyName}"
                itemNewFeedBinding.content.text = singlePost.feedContent
                itemNewFeedBinding.title.text = singlePost.feedTitle
                itemNewFeedBinding.bio.text = singlePost.postedByCompany.companyMotto
            }
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}