package com.ssip.buzztalk.ui.fragments.chat.findusers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentMessagesBinding
import com.ssip.buzztalk.databinding.FragmentStartNewConversationBinding
import com.ssip.buzztalk.models.connections.response.allConnections.Connection
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.ui.fragments.detailedrelation.DetailedRelationConnectionsAdapter
import com.ssip.buzztalk.ui.fragments.profile.ProfileViewModel
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartNewConversationFragment : Fragment() {

    private var _binding: FragmentStartNewConversationBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var startNewConversationAdapter: StartNewConversationAdapter

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var glide: RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartNewConversationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.personsRecyclerView.setHasFixedSize(true)

        profileViewModel.getAllConnections(
            tokenManager.getTokenWithBearer()!!,
            UserID(tokenManager.getUserId()!!)
        )

        profileViewModel.allConnections.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    startNewConversationAdapter = StartNewConversationAdapter(
                        glide,
                        tokenManager.getUserId()!!,
                        navigate = { userId ->
                            navigate(userId)
                        },
                    )
                    startNewConversationAdapter.users = response.data?.data?.connections as MutableList<Connection>
                    binding.personsRecyclerView.adapter = startNewConversationAdapter
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }
    }

    private fun navigate(userId: String) {
        val action = StartNewConversationFragmentDirections.actionStartNewConversationFragmentToMessagingFragment(userId)
        findNavController().navigate(action)
    }
}