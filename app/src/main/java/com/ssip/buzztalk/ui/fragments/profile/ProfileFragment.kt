package com.ssip.buzztalk.ui.fragments.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddEmailBinding
import com.ssip.buzztalk.databinding.FragmentProfileBinding
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.ui.fragments.detailedrelation.DetailedRelationFragmentArgs
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getUserInfo(tokenManager.getTokenWithBearer()!!)

        val appBarLayout: AppBarLayout = binding.mainAppbar
        val toolbar: Toolbar = binding.toolbarTop

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val isShow = true
            val shouldShowIcon = abs(verticalOffset) >= 396

            toolbar.navigationIcon = if (isShow && shouldShowIcon) {
                resources.getDrawable(R.drawable.user_top_bar, null)
            } else {
                resources.getDrawable(R.color.transparant)
            }
        })

        profileViewModel.userInfo.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    binding.mainCollapsing.title = response.data!!.data.user.firstName + " " +response.data.data.user.lastName
                    Log.d(" BIO VALUE ", "onViewCreated: ${response.data.data.user.bio}")
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        profileViewModel.getFollowersFollowingCount(tokenManager.getTokenWithBearer()!!, UserID(tokenManager.getUserId()!!))

        profileViewModel.totalFollowersFollowingCount.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    binding.followers.text = response.data!!.data.followers.size.toString()
                    binding.following.text = response.data.data.following.size.toString()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        profileViewModel.getAllConnections(tokenManager.getTokenWithBearer()!!, UserID(tokenManager.getUserId()!!))

        profileViewModel.allConnections.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    binding.connections.text = response.data!!.data.connections.size.toString()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
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
}