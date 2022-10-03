package com.ssip.buzztalk.ui.fragments.chat.messages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Transformations
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentChatsBinding
import com.ssip.buzztalk.databinding.FragmentMessagesBinding
import com.ssip.buzztalk.models.chat.MessageModel
import com.ssip.buzztalk.models.chat.request.MessageBody
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.models.chat.response.messages.Message
import com.ssip.buzztalk.models.chat.response.newmessage.NewMessage
import com.ssip.buzztalk.ui.fragments.chat.ChatViewModel
import com.ssip.buzztalk.ui.fragments.userdetailprofile.UserDetailProfileFragmentArgs
import com.ssip.buzztalk.utils.Constants
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class MessagingFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    private lateinit var messageAdapter: MessageAdapter
    private val messageViewModel: MessageViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val args: MessagingFragmentArgs by navArgs()

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var gson: Gson
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("SOCKET ID ", socket.id())

        binding.messageRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.messageRecyclerView.setHasFixedSize(true)

        messageViewModel.getAllMessages(To(args.toId))

        messageAdapter = MessageAdapter(tokenManager.getUserId()!!)

        binding.onlineMessageToolBar.topOnlineMessageBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        chatViewModel.getUserStatus(To(args.toId))

        chatViewModel.userStatus.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    if (response.data?.message == Constants.USER_ONLINE) {
                        binding.onlineMessageToolBar.status.text = "Online"
                    } else {
                        binding.onlineMessageToolBar.status.text = "Offline"
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        messageViewModel.allMessages.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    if(response.data!!.data.messages.isNotEmpty()){
                        if (response.data?.data?.messages?.get(0)?.from == args.toId) {
                            binding.onlineMessageToolBar.nameOfUser.text = "${response.data?.data?.messages?.get(0)?.fromObj?.get(0)?.firstName} ${response.data?.data?.messages?.get(0)?.fromObj?.get(0)?.lastName}"
                        } else {
                            binding.onlineMessageToolBar.nameOfUser.text = "${response.data?.data?.messages?.get(0)?.toObj?.get(0)?.firstName} ${response.data?.data?.messages?.get(0)?.toObj?.get(0)?.lastName}"
                        }
                        val messages = response.data?.data?.messages?.map { message ->
                            MessageModel(message.from, message.body)
                        }
//                    messageAdapter.messages = response.data?.data?.messages as MutableList<Message>
                        messageAdapter.messages = messages as MutableList<MessageModel>
                        binding.messageRecyclerView.adapter = messageAdapter
                        binding.messageRecyclerView.scrollToPosition(messageAdapter.messages.size - 1)
                        binding.messageListProgressBar.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    binding.messageListProgressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.messageListProgressBar.visibility = View.GONE
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                messageViewModel.messages.collect { message ->
//                    withContext(Dispatchers.Main) {
//                        messageAdapter.addNewMessage(message!!)
//                    }
//                }
//            }
//        }

        lifecycleScope.launch {
            socket.off(Constants.NEW_MESSAGE).on(Constants.NEW_MESSAGE) { message ->
                val message = gson.fromJson(message[0].toString(), NewMessage::class.java)
                messageAdapter.addNewMessage(MessageModel(message.data.message.from, message.data.message.body))
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        binding.messageRecyclerView.scrollToPosition(messageAdapter.messages.size - 1)
                    }
                }
            }
        }

        binding.send.setOnClickListener {
            val messageContent = binding.editTextChatMessage.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                messageViewModel.sendMessage(MessageBody(
                    args.toId,
                    messageContent
                ))
                messageAdapter.addNewMessage(MessageModel(tokenManager.getUserId()!!, messageContent))
                binding.messageRecyclerView.scrollToPosition(messageAdapter.messages.size - 1)
                binding.editTextChatMessage.setText("")
            }
        }

        messageViewModel.sendMessage.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }
    }
}