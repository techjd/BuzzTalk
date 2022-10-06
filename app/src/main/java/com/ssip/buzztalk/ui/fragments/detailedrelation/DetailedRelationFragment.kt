package com.ssip.buzztalk.ui.fragments.detailedrelation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentDetailedRelationBinding
import com.ssip.buzztalk.databinding.FragmentProfileBinding
import com.ssip.buzztalk.models.connections.response.allConnections.Connection
import com.ssip.buzztalk.models.followers.request.FolloweeId
import com.ssip.buzztalk.models.followers.response.Follower
import com.ssip.buzztalk.models.followers.response.FollowerId
import com.ssip.buzztalk.models.followers.response.Followers
import com.ssip.buzztalk.models.following.response.Following
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.ui.fragments.search.SearchFragmentDirections
import com.ssip.buzztalk.ui.fragments.userdetailprofile.UserDetailProfileFragmentArgs
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailedRelationFragment : Fragment() {

    private var _binding: FragmentDetailedRelationBinding? = null
    private val binding get() = _binding!!
    private val args: DetailedRelationFragmentArgs by navArgs()
    private val detailedRelationViewModel: DetailedRelationViewModel by viewModels()
    private lateinit var detailedRelationConnectionsAdapter: DetailedRelationConnectionsAdapter
    private lateinit var detailedRelationFollowerAdapter: DetailedRelationFollowerAdapter
    private lateinit var detailedRelationFollowingAdapter: DetailedRelationFollowingAdapter

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
        _binding = FragmentDetailedRelationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.relationsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.relationsRecyclerView.setHasFixedSize(true)

        when (args.type) {
            0 -> {
                binding.detailedRelationToolBar.title.text = "\uD83D\uDC68\u200D\uD83D\uDCBC Followers"
                detailedRelationViewModel.getAllFollowers(
                    tokenManager.getTokenWithBearer()!!,
                    FolloweeId(args.userId)
                )
            }
            1 -> {
                binding.detailedRelationToolBar.title.text = "\uD83D\uDC68\u200D\uD83D\uDCBC Following"
                detailedRelationViewModel.getAllFollowing(
                    tokenManager.getTokenWithBearer()!!,
                    com.ssip.buzztalk.models.following.request.FollowerId(args.userId)
                )
            }
            2 -> {
                binding.detailedRelationToolBar.title.text = "\uD83E\uDD1D Connections"
                detailedRelationViewModel.getAllConnections(tokenManager.getTokenWithBearer()!!, UserID(args.userId))
            }
        }

        detailedRelationViewModel.allConnections.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    detailedRelationConnectionsAdapter = DetailedRelationConnectionsAdapter(
                        glide,
                        args.userId,
                        navigate = { userId ->
                            navigate(userId)
                        },
                    )
                    detailedRelationConnectionsAdapter.users = response.data?.data?.connections as MutableList<Connection>
                    binding.relationsRecyclerView.adapter = detailedRelationConnectionsAdapter
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        detailedRelationViewModel.allFollowers.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    detailedRelationFollowerAdapter = DetailedRelationFollowerAdapter(
                        glide,
                        navigate = { userId ->
                            navigate(userId)
                        },
                    )
                    detailedRelationFollowerAdapter.users = response.data?.data?.followers as MutableList<Follower>
                    binding.relationsRecyclerView.adapter = detailedRelationFollowerAdapter
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        detailedRelationViewModel.allFollowing.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    detailedRelationFollowingAdapter = DetailedRelationFollowingAdapter(
                        glide,
                        navigate = { userId ->
                            navigate(userId)
                        },
                    )
                    detailedRelationFollowingAdapter.users = response.data?.data?.following as MutableList<Following>
                    binding.relationsRecyclerView.adapter = detailedRelationFollowingAdapter
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
        val action = DetailedRelationFragmentDirections.actionDetailedRelationFragmentToUserDetailProfileFragment(userId)
        findNavController().navigate(action)
    }
}