package com.ssip.buzztalk.ui.fragments.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentChatsBinding
import com.ssip.buzztalk.databinding.FragmentNotificationsBinding
import com.ssip.buzztalk.models.notifications.response.Data
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.NetworkResult
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val notificationsViewModel: NotificationsViewModel by viewModels()
    private lateinit var notificationsAdapter: NotificationsAdapter

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardView.setOnClickListener {
            findNavController().navigate(R.id.action_notificationsFragment_to_connectionRequestsFragment)
        }

        with(binding) {
            notiRv.layoutManager = LinearLayoutManager(requireContext())
            notiRv.setHasFixedSize(true)
        }

        notificationsViewModel.getUserNotifications(tokenManager.getTokenWithBearer()!!)

        notificationsViewModel.userNotifications.observe(viewLifecycleOwner) { response ->
            when(response.status)  {
                SUCCESS -> {
                    notificationsAdapter = NotificationsAdapter { postId ->
                        navigateToPost(postId)
                    }
                    notificationsAdapter.notifications = response.data?.data as MutableList<Data>
                    binding.notiRv.adapter = notificationsAdapter
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }
    }

    private fun navigateToPost(postId: String) {
        // navigate
    }
}