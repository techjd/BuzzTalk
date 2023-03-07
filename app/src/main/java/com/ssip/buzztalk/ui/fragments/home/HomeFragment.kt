package com.ssip.buzztalk.ui.fragments.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddEmailBinding
import com.ssip.buzztalk.databinding.FragmentHomeBinding
import com.ssip.buzztalk.models.chat.request.MakeUserOnline
import com.ssip.buzztalk.models.feed.response.FeedX
import com.ssip.buzztalk.models.likes.request.PostId
import com.ssip.buzztalk.models.newfeed.Feed
import com.ssip.buzztalk.models.notifications.request.NotificationBody
import com.ssip.buzztalk.ui.components.Event
import com.ssip.buzztalk.ui.components.HomeBottomSheet
import com.ssip.buzztalk.ui.components.HomeBottomSheetViewModel
import com.ssip.buzztalk.ui.fragments.chat.ChatViewModel
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationsFragmentDirections
import com.ssip.buzztalk.ui.fragments.home.adapter.FeedAdapter
import com.ssip.buzztalk.ui.fragments.home.adapter.PostsAdapter
import com.ssip.buzztalk.ui.fragments.post.PostViewModel
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()
    private val homeBottomSheetViewModel: HomeBottomSheetViewModel by activityViewModels()

    private val chipsCategoriesList = listOf<String>(
      "University",
      "School",
      "Working Professional",
      "Research Scholars",
      "Professor",
      "Student"
    )

    private lateinit var postsAdapter: PostsAdapter
    private lateinit var feedAdapter: FeedAdapter

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var homeBottomSheet: HomeBottomSheet

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
                    lifecycleScope.launch {
                        socket.emit("grp", "make me online in groups")
                    }
                    findNavController().navigate(R.id.action_homeFragment_to_chatsFragment)
                    true
                }
                else -> false
            }
        }

        binding.filterLinearLl.setOnClickListener {
            homeBottomSheet.show(parentFragmentManager, "BOTTOM SHEET")
            homeBottomSheet
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

        // homeBottomSheetViewModel.clickEvent.observe(viewLifecycleOwner) {
        //     Log.d("EVENT REV", "onViewCreated: ")
        //     postViewModel.filterFeed(homeBottomSheetViewModel.items.value as List<String>)
        // }

        lifecycleScope.launch {
            homeBottomSheetViewModel.events.collect {
                when(it) {
                    Event.Click -> {
                        postViewModel.filterFeed(homeBottomSheetViewModel.items.value as MutableList<String>)
                        Log.d("CLICK ", "onViewCreated: ")
                    }
                }
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

        postViewModel.filteredFeed.observe(viewLifecycleOwner) {
            // postsAdapter.posts = it.data.feed
            postsAdapter.changeList(it.data.feed)
            Log.d("CLICK HERE", "onViewCreated: ${it.data.feed}")
            // postsAdapter.notifyDataSetChanged()
        }

        lifecycleScope.launch {
            if (socket.id() != null) {
                chatViewModel.makeMeOnline(MakeUserOnline(socket.id()))
            }
        }

       postViewModel.getFeed()

        // postViewModel.getNewFeed()

        postViewModel.likePost.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                SUCCESS -> {
                    postViewModel.getFeed()
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        postViewModel.feed.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    postsAdapter = PostsAdapter(
                        { id ->
                            navigate(id)
                        },
                        { postId ->
                            postViewModel.likePost(PostId(postId))

                        },
                      requireContext()
                    )
                    if (response?.data!!.data!!.feed!!.isEmpty()) {
                        binding.noPosts.visibility = View.VISIBLE
                    } else {
                        postsAdapter.posts = response.data?.data?.feed as MutableList<FeedX>
                        binding.feedRv.adapter = postsAdapter
                    }
                    binding.shimmerLayout2.visibility = View.GONE
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    binding.shimmerLayout2.visibility = View.GONE
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        // postViewModel.newFeed.observe(viewLifecycleOwner) { response ->
        //     when(response.status) {
        //         Status.SUCCESS -> {
        //             feedAdapter = FeedAdapter(
        //                 { id ->
        //                     navigate(id)
        //                 },
        //                 { text ->
        //                     navigateToUser(text)
        //                 }
        //             )
        //             if (response?.data!!.data!!.feed!!.isEmpty()) {
        //                 binding.noPosts.visibility = View.VISIBLE
        //             } else {
        //                 feedAdapter.posts = response.data?.data?.feed as MutableList<Feed>
        //                 binding.feedRv.adapter = feedAdapter
        //             }
        //         }
        //         Status.LOADING -> {
        //
        //         }
        //         Status.ERROR -> {
        //             DialogClass(view).showDialog(response.message!!)
        //         }
        //     }
        // }

        Log.d("SOCKET ID", "onViewCreated: ${socket.id()}")
    }

    private fun navigateToUser(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun navigate(id: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailedPostFragment(id)
        findNavController().navigate(action)
        // Toast.makeText(context, "Navigate to detail screen", Toast.LENGTH_SHORT).show()
    }

    fun ChipGroup.addChip(context: Context, label: String){
        Chip(context).apply {
            id = View.generateViewId()
            text = label
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }
}