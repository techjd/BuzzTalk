package com.ssip.buzztalk.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddEmailBinding
import com.ssip.buzztalk.databinding.FragmentHomeBinding
import com.ssip.buzztalk.models.chat.request.MakeUserOnline
import com.ssip.buzztalk.models.feed.response.FeedX
import com.ssip.buzztalk.models.notifications.request.NotificationBody
import com.ssip.buzztalk.ui.fragments.chat.ChatViewModel
import com.ssip.buzztalk.ui.fragments.home.adapter.PostsAdapter
import com.ssip.buzztalk.ui.fragments.post.PostViewModel
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()

    private lateinit var postsAdapter: PostsAdapter

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeToolBar.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.homeToolBar.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.messages -> {
                    findNavController().navigate(R.id.action_homeFragment_to_chatsFragment)
                    true
                }
                else -> false
            }
        }

        binding.feedRv.layoutManager = LinearLayoutManager(context)
        binding.feedRv.setHasFixedSize(true)

        runBlocking {
            var isNull = true;
            while (isNull) {
                if (socket.id() != null) {
                    isNull = false
                }
            }
        }

        lifecycleScope.launch {
            socket.on("connect") { response ->
                Log.d(" CONNECTION AGAIN ", "onViewCreated: ${socket.id()}")
                chatViewModel.makeMeOnline(MakeUserOnline(socket.id()))
            }
        }

        lifecycleScope.launch{
            if (tokenManager.getUserFirstTime()) {
                homeViewModel.sendNoti(tokenManager.getTokenWithBearer()!!, NotificationBody(tokenManager.getNotificationTokenOnCreate()!!))
                tokenManager.saveUserFirstTime(false)
            } else {
                if (tokenManager.getNotificationTokenOnCreate() != tokenManager.getNotificationTokenOnNewToken()) {
                    homeViewModel.sendNoti(tokenManager.getTokenWithBearer()!!, NotificationBody(tokenManager.getNotificationTokenOnCreate()!!))
                }
            }
        }

        lifecycleScope.launch {
            if (socket.id() != null) {
                chatViewModel.makeMeOnline(MakeUserOnline(socket.id()))
            }
        }

        postViewModel.getFeed()

        postViewModel.feed.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    postsAdapter = PostsAdapter(
                        { id ->
                            navigate(id)
                        },
                        { text ->
                            navigateToUser(text)
                        }
                    )
                    if (response?.data!!.data!!.feed!!.isEmpty()) {
                        binding.noPosts.visibility = View.VISIBLE
                    } else {
                        postsAdapter.posts = response.data?.data?.feed as MutableList<FeedX>
                        binding.feedRv.adapter = postsAdapter
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        Log.d("SOCKET ID", "onViewCreated: ${socket.id()}")
    }

    private fun navigateToUser(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun navigate(id: String) {
        Toast.makeText(context, "Navigate to detail screen", Toast.LENGTH_SHORT).show()
    }
}