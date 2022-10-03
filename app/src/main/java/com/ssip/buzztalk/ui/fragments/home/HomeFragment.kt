package com.ssip.buzztalk.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddEmailBinding
import com.ssip.buzztalk.databinding.FragmentHomeBinding
import com.ssip.buzztalk.models.chat.request.MakeUserOnline
import com.ssip.buzztalk.ui.fragments.chat.ChatViewModel
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

    @Inject
    lateinit var socket: Socket

    @Inject
    lateinit var gson: Gson

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

        lifecycleScope.launch {
            if (socket.id() != null) {
                chatViewModel.makeMeOnline(MakeUserOnline(socket.id()))
            }
        }

        Log.d("SOCKET ID", "onViewCreated: ${socket.id()}")
    }
}