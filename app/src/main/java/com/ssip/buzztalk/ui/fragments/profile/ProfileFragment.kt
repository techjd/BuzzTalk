package com.ssip.buzztalk.ui.fragments.profile

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ssip.buzztalk.databinding.FragmentProfileBinding
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var profileFeedAdapter: ProfileFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getUserInfo(tokenManager.getTokenWithBearer()!!)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            Log.d("LOC AG", "onViewCreated: Eantered")
            location?.let {
                Log.d("LOC AGG", "onViewCreated: Entered into the blll")

                val geocoder = Geocoder(requireContext())
                geocoder.getFromLocation(location.latitude, location.longitude, 1).forEach { address ->
                    address?.let { addr ->
                        val cityName = addr.locality
                        val stateName = addr.adminArea
                        val countryName = addr.countryName

                        val location = "$cityName , $stateName, $countryName"
                        Log.d("LOC ", "onViewCreated: ${location}")

                        binding.location.text = location
                        binding.location.visibility = View.VISIBLE
                        binding.staticLogo.visibility = View.VISIBLE
                    }
                }
            }
        }

        with(binding) {
            myPostsRv.layoutManager = LinearLayoutManager(context)
            myPostsRv.setHasFixedSize(true)
        }


        val typeface = ResourcesCompat.getFont(requireContext(), com.ssip.buzztalk.R.font.nunitosans_semibold)

        profileViewModel.userInfo.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                SUCCESS -> {
                    with(binding) {
                        mainCollapsing.title = response.data!!.data.user.firstName + " " +response.data.data.user.lastName
                        mainCollapsing.setCollapsedTitleTypeface(typeface)
                        mainCollapsing.setExpandedTitleTypeface(typeface)
                    }
                    val bio = response.data!!.data.user.userType
                    binding.bio.text = bio.substring(0, bio.length - 1)
                    Log.d(" BIO VALUE ", "onViewCreated: ${response.data.data.user.bio}")
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        profileViewModel.getFollowersFollowingCount(tokenManager.getTokenWithBearer()!!, UserID(tokenManager.getUserId()!!))

        profileViewModel.totalFollowersFollowingCount.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                SUCCESS -> {
                    binding.followers.text = response.data!!.data.followers.size.toString()
                    binding.following.text = response.data.data.following.size.toString()
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        profileViewModel.getAllConnections(tokenManager.getTokenWithBearer()!!, UserID(tokenManager.getUserId()!!))

        profileViewModel.allConnections.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                SUCCESS -> {
                    binding.connections.text = response.data!!.data.connections.size.toString()
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        profileViewModel.getMyFeed()

        profileViewModel.myFeed.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                SUCCESS -> {
                    profileFeedAdapter = ProfileFeedAdapter { postId ->
                        navigate(postId)
                    }
                    if (response.data?.data?.feed?.isEmpty()!!) {
                        binding.textView2.visibility = View.VISIBLE
                    } else {
                        profileFeedAdapter.posts = response.data.data.feed as MutableList<com.ssip.buzztalk.models.myfeed.response.Feed>

                        with(binding) {
                            myPostsRv.adapter = profileFeedAdapter
                            myPostsRv.visibility = View.VISIBLE
                            textView2.visibility = View.GONE
                        }
                    }
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        binding.followersBlock.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToDetailedRelationFragment(0, tokenManager.getUserId()!!)
            findNavController().navigate(action)
        }

        binding.followingBlock.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToDetailedRelationFragment(1, tokenManager.getUserId()!!)
            findNavController().navigate(action)
        }

        binding.connectionsBlock.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToDetailedRelationFragment(2, tokenManager.getUserId()!!)
            findNavController().navigate(action)
        }
    }

    private fun navigate(postId: String) {
        // navigate to detailed postId
    }
}