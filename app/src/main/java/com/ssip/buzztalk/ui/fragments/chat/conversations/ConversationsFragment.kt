package com.ssip.buzztalk.ui.fragments.chat.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.tabs.TabLayoutMediator
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentChatsBinding
import com.ssip.buzztalk.ui.fragments.chat.ConversationsViewPagerAdapter
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConversationsFragment : Fragment() {

  private var _binding: FragmentChatsBinding? = null
  private val binding get() = _binding!!
  private val conversationViewModel: ConversationViewModel by viewModels()
  private lateinit var conversationsAdapter: ConversationsAdapter

  private lateinit var conversationsViewPagerAdapter: ConversationsViewPagerAdapter

  private var areOptionsVisible: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?,
  ): View? {
    _binding = FragmentChatsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view, savedInstanceState)

    conversationsViewPagerAdapter = ConversationsViewPagerAdapter(this)
    binding.viewPager.adapter = conversationsViewPagerAdapter
    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, postion ->
      when (postion) {
        0 -> tab.text = "Chats"
        else -> tab.text = "Groups"
      }
    }.attach()

    binding.floatingActionButton.setOnClickListener {
      if (areOptionsVisible) {
        binding.newConversationFab.visibility = View.VISIBLE
        binding.newGroupFab.visibility = View.VISIBLE
        areOptionsVisible = false
      } else {
        binding.newConversationFab.visibility = View.INVISIBLE
        binding.newGroupFab.visibility = View.INVISIBLE
        areOptionsVisible = true
      }
    }

    binding.newConversationFab.setOnClickListener {
      findNavController().navigate(R.id.action_chatsFragment_to_startNewConversationFragment)
    }

    binding.newGroupFab.setOnClickListener {
      findNavController().navigate(R.id.action_chatsFragment_to_newGroupFragment)
    }
  }
}