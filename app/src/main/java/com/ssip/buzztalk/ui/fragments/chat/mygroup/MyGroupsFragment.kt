package com.ssip.buzztalk.ui.fragments.chat.mygroup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.databinding.FragmentMyGroupsBinding
import com.ssip.buzztalk.models.groupchat.response.allgroups.AllGroup
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationViewModel
import com.ssip.buzztalk.ui.fragments.chat.conversations.ConversationsFragmentDirections
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyGroupsFragment : Fragment() {

  private var _binding: FragmentMyGroupsBinding? = null
  private val binding get() = _binding!!
  private lateinit var myGroupsAdapter: MyGroupsAdapter

  private val conversationViewModel: ConversationViewModel by viewModels()

  @Inject
  lateinit var glide: RequestManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    _binding = FragmentMyGroupsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    binding.apply {
      groupsRecyclerView.layoutManager = LinearLayoutManager(context)
      groupsRecyclerView.setHasFixedSize(true)
    }

    conversationViewModel.getAllGroups()

    conversationViewModel.allGroups.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        Status.SUCCESS -> {
          myGroupsAdapter = MyGroupsAdapter(
            glide,
            navigate = { groupId ->
              navigate(groupId)
            }
          )
          myGroupsAdapter.groups = response.data?.data?.allGroups as MutableList<AllGroup>
          binding.groupsRecyclerView.adapter = myGroupsAdapter
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

  private fun navigate(groupId: String) {
    val action = ConversationsFragmentDirections.actionChatsFragmentToGroupMessagesFragment(groupId)
    findNavController().navigate(action)
  }
}