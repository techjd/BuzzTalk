package com.ssip.buzztalk.ui.fragments.chat.mygroup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentGroupMessagesBinding
import com.ssip.buzztalk.databinding.FragmentMessagesBinding
import com.ssip.buzztalk.databinding.FragmentMyGroupsBinding
import com.ssip.buzztalk.models.chat.MessageModel
import com.ssip.buzztalk.models.chat.response.newmessage.NewMessage
import com.ssip.buzztalk.models.groupchat.request.GroupMessageBody
import com.ssip.buzztalk.models.groupchat.request.SendMessageGroupRequest
import com.ssip.buzztalk.models.groupchat.response.newgroupmessage.NewGroupMessage
import com.ssip.buzztalk.ui.fragments.chat.ChatViewModel
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationViewModel
import com.ssip.buzztalk.ui.fragments.chat.messages.MessagingFragmentArgs
import com.ssip.buzztalk.utils.Constants
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class GroupMessagesFragment : Fragment() {

  private var _binding: FragmentGroupMessagesBinding? = null
  private val binding get() = _binding!!

  private val conversationViewModel: ConversationViewModel by viewModels()
  private val groupInfoViewModel: GroupInfoViewModel by viewModels()
  private val args: GroupMessagesFragmentArgs by navArgs()

  private lateinit var groupMessagesAdapter: GroupMessagesAdapter

  @Inject
  lateinit var tokenManager: TokenManager

  @Inject
  lateinit var socket: Socket

  @Inject
  lateinit var gson: Gson

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
      socket.emit("leave", args.groupId)
      findNavController().popBackStack()
    }

    conversationViewModel.getAllGroupConvo(args.groupId)
    groupInfoViewModel.getSingleGroupInfo(args.groupId)

    groupMessagesAdapter = GroupMessagesAdapter(tokenManager.getUserId()!!)

    binding.send.setOnClickListener {
      val msg = binding.editTextChatMessage.text.toString().trim()
      if (msg.isNotEmpty()) {
        conversationViewModel.sendGroupMessage(
          SendMessageGroupRequest(msg),
          args.groupId
        )
      } else {
        Toast.makeText(context, "Please Enter Few Chars", Toast.LENGTH_SHORT).show()
      }
    }

    conversationViewModel.sendGroupMessageResponse.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        SUCCESS -> {
          binding.editTextChatMessage.text.clear()
        }
        LOADING -> {

        }
        ERROR -> {
          DialogClass(view).showDialog(response.message!!)
        }
      }
    }

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
            hideProgressBar()
          }
        }
        LOADING -> {
          showProgressBar()
        }
        ERROR -> {
          hideProgressBar()
          DialogClass(view).showDialog(response.message!!)
        }
      }
    }


    lifecycleScope.launch {
      socket.off(Constants.NEW_MESSAGE).on(Constants.NEW_MESSAGE) { msg ->
        val message = gson.fromJson(msg[0].toString(), NewGroupMessage::class.java)
        Log.d("GRP MSG ", "onViewCreated: ${msg[0].toString()}")
        val userName = "${message.data.message.userId.firstName} ${message.data.message.userId.lastName}"
        groupMessagesAdapter.addNewMessage(
          GroupMessageBody(
            message.data.message.userId._id,
            userName,
            message.data.message.message
          )
        )
        lifecycleScope.launch {
          withContext(Dispatchers.Main) {
            binding.messageRecyclerView.scrollToPosition(groupMessagesAdapter.messages.size - 1)
          }
        }
      }
    }

    groupInfoViewModel.singleGroupInfo.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        SUCCESS -> {
          binding.topGroupMessageToolBar.groupName.text = response.data!!.data.groupDetails.groupName
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
          socket.emit("leave", args.groupId)
          findNavController().popBackStack()
        }
      })
  }

  private fun showProgressBar() {
    binding.groupMessageListProgressBar.visibility = View.VISIBLE
  }

  private fun hideProgressBar() {
    binding.groupMessageListProgressBar.visibility = View.INVISIBLE
  }
}