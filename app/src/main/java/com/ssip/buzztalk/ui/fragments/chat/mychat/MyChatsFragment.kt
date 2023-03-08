package com.ssip.buzztalk.ui.fragments.chat.mychat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentMyChatsBinding
import com.ssip.buzztalk.databinding.FragmentMyGroupsBinding
import com.ssip.buzztalk.models.chat.response.conversations.Conversation
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationViewModel
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationsAdapter
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationsFragmentDirections
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyChatsFragment : Fragment() {

  private val conversationViewModel: ConversationViewModel by viewModels()
  private lateinit var conversationsAdapter: ConversationsAdapter

  @Inject
  lateinit var glide: RequestManager

  @Inject
  lateinit var tokenManager: TokenManager

  private var _binding: FragmentMyChatsBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    _binding = FragmentMyChatsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view, savedInstanceState)

    binding.conversationRecyclerView.layoutManager = LinearLayoutManager(context)
    binding.conversationRecyclerView.setHasFixedSize(true)
    conversationViewModel.getAllConversations()

    conversationViewModel.allConversations.observe(viewLifecycleOwner) { response ->
        when(response.status) {
            Status.SUCCESS -> {
                conversationsAdapter = ConversationsAdapter(
                    glide,
                    tokenManager.getUserId()!!,
                    navigate = { toId ->
                        navigate(toId)
                    }
                )
                conversationsAdapter.conversations = response.data?.data?.conversations as MutableList<Conversation>
                binding.conversationRecyclerView.adapter = conversationsAdapter
                binding.progressBar.visibility = View.GONE
                Log.d("SUCCESS ", " REQUEST COMPLETED  ")
            }
            Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                DialogClass(view).showDialog(response.message!!)
            }
        }
    }
  }

  private fun navigate(toId: String) {
    val action = ConversationsFragmentDirections.actionChatsFragmentToMessagingFragment(toId)
    findNavController().navigate(action)
  }
}