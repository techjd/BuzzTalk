package com.ssip.buzztalk.ui.fragments.detailedpost

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentDetailedPostBinding
import com.ssip.buzztalk.databinding.FragmentMessagesBinding
import com.ssip.buzztalk.models.comment.request.CommentRequest
import com.ssip.buzztalk.models.comment.response.Data
import com.ssip.buzztalk.models.hashtags.request.HashIdBody
import com.ssip.buzztalk.ui.fragments.hashtags.HashTagsViewModel
import com.ssip.buzztalk.ui.fragments.home.HomeFragmentDirections
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedPostFragment : Fragment() {

  private var _binding: FragmentDetailedPostBinding? = null
  private val binding get() = _binding!!
  private val detailedPostFragmentArgs: DetailedPostFragmentArgs by navArgs()
  private val detailedPostViewModel: DetailedPostViewModel by viewModels()
  private val hashTagsViewModel: HashTagsViewModel by viewModels()

  private lateinit var commentAdapter: CommentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentDetailedPostBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    detailedPostViewModel.getSinglePost(detailedPostFragmentArgs.postId)

    detailedPostViewModel.singlePost.observe(viewLifecycleOwner) { response ->
        when(response.status) {
          SUCCESS -> {
            with(binding) {
              response?.let {
                val data = response.data?.data?.post
                val fullName = "${response.data?.data?.post?.userId?.firstName} ${response.data?.data?.post?.userId?.lastName}"
                userName.text = fullName
                bio.text = data?.userId?.userType
                content.text = data?.content

                var content = data?.content!!

                var spannable = SpannableString(content)

                var prevIdx = -1

                for(i in 0..content.length-1) {
                  // Log.d("CONTENT", "onBindViewHolder: ${content[i]}")
                  if (content[i] == ' ') {
                    if (prevIdx != -1) {

                      val substr = content.substring(prevIdx, i)

                      val clickableStr = object : ClickableSpan() {
                        override fun onClick(view: View) {
                          if (substr[0] == '#') {
                            // Toast.makeText(context, substr, Toast.LENGTH_SHORT).show()

                            hashTagsViewModel.getHashId(HashIdBody(substr.substring(1)))
                          }
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
                    val text = content.substring(prevIdx, content.length)
                    if (text[0] == '#') {
                      // Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                      hashTagsViewModel.getHashId(HashIdBody(text.substring(1)))
                    }
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

                binding.content.text = spannable
                binding.content.setMovementMethod(LinkMovementMethod.getInstance())

                totalLikesCnt.text = data?.likes.toString()
                totalComments.text = "${data?.comments.toString()} comments"
              }
            }
          }
          LOADING -> {

          }
          ERROR -> {
            DialogClass(view).showDialog(response.message!!)
          }
        }
    }

    hashTagsViewModel.hashId.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        SUCCESS -> {
          val action = DetailedPostFragmentDirections.actionDetailedPostFragmentToHashTagsFragment2(response.data?.data?._id!!, response.data?.data?.hashTag!!)
          findNavController().navigate(action)
        }
        LOADING -> {

        }
        ERROR -> {
          DialogClass(view).showDialog(response.message!!)
        }
      }
    }

    with(binding) {
      commentsRv.layoutManager = LinearLayoutManager(requireContext())
      commentsRv.setHasFixedSize(true)

      btnComment.setOnClickListener {
        val comment = edtTxtComment.text.toString()
        if (comment.isNullOrEmpty()) {
          Toast.makeText(requireContext(), "Comment can't be empty", Toast.LENGTH_SHORT).show()
        } else {
          detailedPostViewModel.commentOnPost(
            CommentRequest(
              content = comment,
              postId = detailedPostFragmentArgs.postId
            )
          )
        }
      }
    }

    detailedPostViewModel.commentSuccess.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        SUCCESS -> {
          Toast.makeText(requireContext(), "Comment Added Successfully", Toast.LENGTH_SHORT).show()
          detailedPostViewModel.getAllComments(detailedPostFragmentArgs.postId)
        }
        LOADING -> {

        }
        ERROR -> {
          DialogClass(view).showDialog(response.message!!)
        }
      }
    }

    detailedPostViewModel.getAllComments(detailedPostFragmentArgs.postId)

    detailedPostViewModel.allComments.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        SUCCESS -> {
          commentAdapter = CommentAdapter()
          commentAdapter.comments = response.data?.data as MutableList<Data>
          binding.commentsRv.adapter = commentAdapter
        }
        LOADING -> {

        }
        ERROR -> {
          DialogClass(view).showDialog(response.message!!)
        }
      }
    }
  }

}