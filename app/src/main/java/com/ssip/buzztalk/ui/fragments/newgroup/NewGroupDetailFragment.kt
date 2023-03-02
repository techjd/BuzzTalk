package com.ssip.buzztalk.ui.fragments.newgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentNewGroupDetailBinding
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewGroupDetailFragment : Fragment() {

  private var _binding: FragmentNewGroupDetailBinding? = null
  private val binding get() = _binding!!

  private val groupViewModel: GroupViewModel by activityViewModels()

  @Inject
  lateinit var tokenManager: TokenManager

  @Inject
  lateinit var glide: RequestManager

  private lateinit var conversationsConnectionsAdapter: ConversationsConnectionsAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentNewGroupDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      groupName.addTextChangedListener {
        groupViewModel.saveGroupName(it.toString().trim())
      }
      groupDescription.addTextChangedListener {
        groupViewModel.saveGroupDescription(it.toString().trim())
      }

      createGroup.setOnClickListener {
        val grpName = groupName.text.toString().trim()
        val grpDes = groupDescription.text.toString().trim()

        if (grpName.isEmpty()) {
          Toast.makeText(context, "Please Fill Group Name", Toast.LENGTH_SHORT).show()
        } else {
          groupViewModel.createGroup(tokenManager.getUserId()!!)
        }
      }
    }

    groupViewModel.createGroupResponse.observe(viewLifecycleOwner) { response ->
      when (response.status) {
        SUCCESS -> {
          Toast.makeText(context, "Group Created SuccessFully", Toast.LENGTH_SHORT).show()
          findNavController().navigate(R.id.action_newGroupDetailFragment_to_chatsFragment)
        }
        ERROR -> {
          DialogClass(view).showDialog(response.message!!)
        }
        LOADING -> {
          // Show Some Kind Of Progress bar over here
        }
      }
    }

    groupViewModel.selectedMembers.observe(viewLifecycleOwner) {
      conversationsConnectionsAdapter =
        ConversationsConnectionsAdapter(glide, tokenManager.getUserId()!!) {
          groupViewModel.addMembers(it)
        }
      conversationsConnectionsAdapter.users = it.data.connections
      binding.selectedGroupMembersRv.layoutManager = LinearLayoutManager(context)
      binding.selectedGroupMembersRv.setHasFixedSize(true)
      binding.selectedGroupMembersRv.adapter = conversationsConnectionsAdapter
    }

    activity?.onBackPressedDispatcher?.addCallback(
      viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          findNavController().navigate(R.id.action_newGroupDetailFragment_to_newGroupFragment)
        }
      })
  }
}