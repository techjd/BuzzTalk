package com.ssip.buzztalk.ui.fragments.connections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentConnectionRequestsBinding
import com.ssip.buzztalk.databinding.FragmentNotificationsBinding
import com.ssip.buzztalk.databinding.FragmentUserDetailProfileBinding
import com.ssip.buzztalk.models.connections.response.connectionrequests.Request
import com.ssip.buzztalk.models.connections.response.connectionrequests.To
import com.ssip.buzztalk.models.connections.response.requestId.RequestId
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionRequestsFragment : Fragment() {

    private var _binding: FragmentConnectionRequestsBinding? = null
    private val binding get() = _binding!!
    private val connectionRequestsViewModel: ConnectionRequestsViewModel by viewModels()

    lateinit var connectionRequestsAdapter: ConnectionRequestsAdapter

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
        _binding = FragmentConnectionRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectionRequestsViewModel.getAllConnectionsRequests(tokenManager.getTokenWithBearer()!!)

        binding.connectionListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.connectionListRecyclerView.setHasFixedSize(true)

        connectionRequestsViewModel.allConnectionRequests.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    if (response.data!!.data.requests.isEmpty()) {
                        binding.staticAllConnectionsRequest.visibility = View.VISIBLE
                    } else {
                        connectionRequestsAdapter = ConnectionRequestsAdapter(
                            glide,
                            accept = { requestId ->
                                accept(requestId)
                            },
                            reject = { requestId ->
                                reject(requestId)
                            }
                        )
                        connectionRequestsAdapter.requests = response.data?.data?.requests as MutableList<Request>
                        binding.connectionListRecyclerView.adapter = connectionRequestsAdapter
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        connectionRequestsViewModel.acceptRequest.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    Toast.makeText(context, "Request Accepted ", Toast.LENGTH_SHORT).show()
                    // TODO - Remove this request from the list of recyclerview
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }
    }

    private fun accept(requestId: String) {
        connectionRequestsViewModel.acceptRequest(tokenManager.getTokenWithBearer()!!, RequestId(requestId))
//        Toast.makeText(context, "Accept The Request ${requestId}", Toast.LENGTH_SHORT).show()
    }

    private fun reject(requestId: String) {
        Toast.makeText(context, "Reject The Request", Toast.LENGTH_SHORT).show()
    }
}