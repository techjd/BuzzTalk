package com.ssip.buzztalk.ui.fragments.chat.mygroup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentGroupMessagesBinding
import com.ssip.buzztalk.databinding.FragmentMessagesBinding
import com.ssip.buzztalk.databinding.FragmentMyGroupsBinding
import com.ssip.buzztalk.models.groupchat.request.GroupMessageBody
import com.ssip.buzztalk.ui.fragments.chat.ChatViewModel
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationViewModel
import com.ssip.buzztalk.ui.fragments.chat.messages.MessagingFragmentArgs
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupMessagesFragment : Fragment() {

  private var _binding: FragmentGroupMessagesBinding? = null
  private val binding get() = _binding!!

  private val conversationViewModel: ConversationViewModel by viewModels()
  private val args: GroupMessagesFragmentArgs by navArgs()

  private lateinit var groupMessagesAdapter: GroupMessagesAdapter

  @Inject
  lateinit var tokenManager: TokenManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentGroupMessagesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    binding.topGroupMessageToolBar.topOnlineGroupMessageBar.setNavigationOnClickListener {
      findNavController().popBackStack()
    }

    conversationViewModel.getAllGroupConvo(args.groupId)

    groupMessagesAdapter = GroupMessagesAdapter(tokenManager.getUserId()!!)

    conversationViewModel.allGroupConvo.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        SUCCESS -> {
          groupMessagesAdapter.messages = response.data?.data?.groupMesages?.map {
            GroupMessageBody(it.userId._id, "${it.userId.firstName} ${it.userId.lastName}", it.message)
          } as MutableList<GroupMessageBody>

          with(binding) {
            messageRecyclerView.layoutManager = LinearLayoutManager(context)
            messageRecyclerView.setHasFixedSize(true)
            messageRecyclerView.adapter = groupMessagesAdapter
          }
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