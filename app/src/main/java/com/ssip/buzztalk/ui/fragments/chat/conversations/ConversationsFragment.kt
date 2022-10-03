package com.ssip.buzztalk.ui.fragments.chat.conversations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentChatsBinding
import com.ssip.buzztalk.models.chat.response.conversations.Conversation
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ConversationsFragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private val conversationViewModel: ConversationViewModel by viewModels()
    private lateinit var conversationsAdapter: ConversationsAdapter

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.conversationRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.conversationRecyclerView.setHasFixedSize(true)
        conversationViewModel.getAllConversations()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_chatsFragment_to_startNewConversationFragment)
        }

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