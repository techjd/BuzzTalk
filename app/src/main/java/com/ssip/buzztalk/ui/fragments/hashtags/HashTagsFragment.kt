package com.ssip.buzztalk.ui.fragments.hashtags

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentDetailedPostBinding
import com.ssip.buzztalk.databinding.FragmentHashTagsBinding
import com.ssip.buzztalk.models.hashtagfeeds.request.HashTagBody
import com.ssip.buzztalk.models.hashtagfeeds.response.Post
import com.ssip.buzztalk.models.hashtagfeeds.response.PostId
import com.ssip.buzztalk.ui.fragments.home.adapter.PostsAdapter
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HashTagsFragment : Fragment() {

  private var _binding: FragmentHashTagsBinding? = null
  private val binding get() = _binding!!
  private val hashTagsViewModel: HashTagsViewModel by viewModels()
  private val hashTagsFragmentArgs: HashTagsFragmentArgs by navArgs()
  private lateinit var hashTagsAdapter: HashTagsAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentHashTagsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      topHashTagToolBar.topHashTagAppBar.title = hashTagsFragmentArgs.hashTags
      // topHashTagToolBar.hashtagName. = hashTagsFragmentArgs.hashTags
      // Log.d("TAG ", "onViewCreated: ${hashTagsFragmentArgs.hashTags}")
      hashTagsRv.layoutManager = LinearLayoutManager(requireContext())
      hashTagsRv.setHasFixedSize(true)
    }

    hashTagsViewModel.getHashTagPosts(HashTagBody(hashTagsFragmentArgs.hashtagId))

    hashTagsViewModel.hashTagsPosts.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        SUCCESS -> {
          hashTagsAdapter = HashTagsAdapter(onClickListener = {postId ->

            })
            binding.hashTagsRv.adapter = hashTagsAdapter
            hashTagsAdapter.posts = response.data!!.data.posts as MutableList<Post>
        }
        LOADING -> {

        }
        ERROR -> {
          DialogClass(view).showDialog(response.message!!)
        }
      }
    }

    activity?.onBackPressedDispatcher?.addCallback(
      viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
      })
  }
}