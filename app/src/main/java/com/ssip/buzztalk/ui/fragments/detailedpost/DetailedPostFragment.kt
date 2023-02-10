package com.ssip.buzztalk.ui.fragments.detailedpost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentDetailedPostBinding
import com.ssip.buzztalk.databinding.FragmentMessagesBinding
import com.ssip.buzztalk.models.comment.request.CommentRequest
import com.ssip.buzztalk.models.comment.response.Data
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