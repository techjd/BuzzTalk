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
import com.ssip.buzztalk.databinding.ItemPostBinding
import com.ssip.buzztalk.models.feed.response.FeedX

class PostsAdapter(
    val onClickListener: (String) -> Unit,
    val showToast: (String) -> Unit
) :
    RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    var posts: MutableList<FeedX> = mutableListOf()

    inner class PostsViewHolder(val itemPostBinding: ItemPostBinding) : RecyclerView.ViewHolder(itemPostBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val singlePost = posts[position]

        with(holder) {
            itemPostBinding.userName.text = "${singlePost.postId.userId.firstName} ${singlePost.postId.userId.lastName}"
//            itemPostBinding.content.setLinkHint(singlePost.postId.content)
            val bio = singlePost.postId.userId.userType
            itemPostBinding.bio.text = "${bio.substring(0, bio.length - 1)}"
            var content = singlePost.postId.content

            var spannable = SpannableString(singlePost.postId.content)

            var prevIdx = -1

            for(i in 0..content.length-1) {
                Log.d("CONTENT", "onBindViewHolder: ${content[i]}")
                if (content[i] == ' ') {
                    if (prevIdx != -1) {

                        val substr = content.substring(prevIdx, i)

                        val clickableStr = object : ClickableSpan() {
                            override fun onClick(view: View) {
                                Toast.makeText(view.context, substr, Toast.LENGTH_SHORT).show()
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                super.updateDrawState(ds)
                                ds.isUnderlineText = false
                            }
                        }

                        spannable.setSpan(
                            clickableStr,
                            prevIdx,
                            i,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        spannable.setSpan (
                            ForegroundColorSpan(Color.parseColor("#03A9F4")),
                            prevIdx, // start
                            i, // end
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                        )
                        prevIdx = -1
                    }
                } else if (content[i] == '@') {
                    Log.d("@ POS", "onBindViewHolder: ${content[i]}")
                    prevIdx = i
                } else if(content[i] == '#') {
                    prevIdx = i
                }
            }

            val clickableStr = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Toast.makeText(view.context, content.substring(prevIdx, content.length), Toast.LENGTH_SHORT).show()
                }
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }

            spannable.setSpan(
                clickableStr,
                prevIdx,
                content.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan (
                ForegroundColorSpan(Color.parseColor("#03A9F4")),
                prevIdx, // start
                content.length, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )


            itemPostBinding.content.text = spannable
            itemPostBinding.content.setMovementMethod(LinkMovementMethod.getInstance())

            itemPostBinding.content.setOnClickListener {
                onClickListener.invoke(singlePost.postId._id)
            }

//            "You can start learning Android from MindOrks"
//            itemPostBinding.content.apply {
//                setText(singlePost.postId.content)
//                setOnLinkClickListener(object : OnLinkClickListener {
//                    override fun onEmailAddressClick(email: String) {
//
//                    }
//
//                    override fun onHashtagClick(hashtag: String) {
//                        showToast(hashtag)
//                    }
//
//                    override fun onMentionClick(mention: String) {
//                        showToast(mention)
//                    }
//
//                    override fun onPhoneClick(phone: String) {
//
//                    }
//
//                    override fun onWebUrlClick(url: String) {
//
//                    }
//
//                })
//            }

//            itemPostBinding.content.text = "${singlePost.postId.content}"
            itemPostBinding.totalLikesCnt.text = singlePost.postId.likes.toString()
            itemPostBinding.totalComments.text = "${singlePost.postId.comments.toString()} comments"
//            itemPostBinding.root.setOnClickListener {
//                onClickListener(singlePost.postId._id)
//            }
//
//            itemPostBinding.content.setOnLinkClickListener { linkType, matchedText ->
//                showToast(matchedText)
//            }
            with(itemPostBinding) {
                bioHeader.setOnClickListener {
                    onClickListener.invoke(singlePost.postId._id)
                }
                mainPost.setOnClickListener {
                    onClickListener.invoke(singlePost.postId._id)
                }

            }

        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}