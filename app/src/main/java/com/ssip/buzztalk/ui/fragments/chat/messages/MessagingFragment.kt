package com.ssip.buzztalk.ui.fragments.chat.messages

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
import com.ssip.buzztalk.databinding.FragmentChatsBinding
import com.ssip.buzztalk.databinding.FragmentMessagesBinding
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.models.chat.response.messages.Message
import com.ssip.buzztalk.ui.fragments.userdetailprofile.UserDetailProfileFragmentArgs
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MessagingFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    private lateinit var messageAdapter: MessageAdapter
    private val messageViewModel: MessageViewModel by viewModels()
    private val args: MessagingFragmentArgs by navArgs()

    @Inject
    lateinit var tokenManager: TokenManager
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

        binding.messageRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.messageRecyclerView.setHasFixedSize(true)

        messageViewModel.getAllMessages(To(args.toId))

        binding.onlineMessageToolBar.topOnlineMessageBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        messageViewModel.allMessages.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    if (response.data?.data?.messages?.get(0)?.from == args.toId) {
                        binding.onlineMessageToolBar.nameOfUser.text = "${response.data?.data?.messages?.get(0)?.fromObj?.get(0)?.firstName} ${response.data?.data?.messages?.get(0)?.fromObj?.get(0)?.lastName}"
                    } else {
                        binding.onlineMessageToolBar.nameOfUser.text = "${response.data?.data?.messages?.get(0)?.toObj?.get(0)?.firstName} ${response.data?.data?.messages?.get(0)?.toObj?.get(0)?.lastName}"
                    }
                    messageAdapter = MessageAdapter(tokenManager.getUserId()!!)
                    messageAdapter.messages = response.data?.data?.messages as MutableList<Message>
                    binding.messageRecyclerView.adapter = messageAdapter
                    binding.messageListProgressBar.visibility = View.GONE
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
    }
}