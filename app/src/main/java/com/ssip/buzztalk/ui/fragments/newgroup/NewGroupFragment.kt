package com.ssip.buzztalk.ui.fragments.newgroup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentNewGroupBinding
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewGroupFragment : Fragment() {

  private var _binding: FragmentNewGroupBinding? = null
  private val binding get() = _binding!!

  private val groupViewModel: GroupViewModel by activityViewModels()
  private lateinit var conversationsConnectionsAdapter: ConversationsConnectionsAdapter
  private lateinit var selectedMembersAdapter: SelectedMembersAdapter

  @Inject
  lateinit var glide: RequestManager

  @Inject
  lateinit var tokenManager: TokenManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentNewGroupBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    binding.yourConnectionsRv.layoutManager = LinearLayoutManager(context)
    binding.yourConnectionsRv.setHasFixedSize(true)

    groupViewModel.selectedMembers.observe(viewLifecycleOwner) {
      if (it.data.connections.isNotEmpty()) {
        selectedMembersAdapter = SelectedMembersAdapter(glide, tokenManager.getUserId()!!) {
          groupViewModel.removeMembers(it)
        }
        selectedMembersAdapter.users = it.data.connections

        with(binding) {
          selectedMembersRv.visibility = View.VISIBLE
          selectedMembersTitle.visibility = View.VISIBLE
          binding.selectedMembersRv.adapter = selectedMembersAdapter
        }
      } else {
        with(binding) {
          selectedMembersRv.visibility = View.GONE
          selectedMembersTitle.visibility = View.GONE
        }
      }
      Log.d("MEMBERS ", "onViewCreated: ${it}")
    }

    binding.newGrpDetailScreen.setOnClickListener {
      findNavController().navigate(R.id.action_newGroupFragment_to_newGroupDetailFragment)
    }

    groupViewModel.getAllConnections(tokenManager.getTokenWithBearer()!!, UserID(tokenManager.getUserId()!!))

    groupViewModel.allConnections.observe(viewLifecycleOwner) { response ->
      when(response.status) {
        SUCCESS -> {
          conversationsConnectionsAdapter = ConversationsConnectionsAdapter(glide, tokenManager.getUserId()!!) {
              groupViewModel.addMembers(it)
          }
          conversationsConnectionsAdapter.users = response.data!!.data.connections
          binding.yourConnectionsRv.adapter = conversationsConnectionsAdapter
        }
        ERROR -> {
          DialogClass(view).showDialog(response.message!!)
        }
        LOADING -> {

        }
      }
    }
  }
}