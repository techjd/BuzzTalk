package com.ssip.buzztalk.ui.fragments.userdetailprofile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentUserDetailProfileBinding
import com.ssip.buzztalk.models.followUnfollow.request.Followee
import com.ssip.buzztalk.models.searchusers.request.OtherUserInfoRequest
import com.ssip.buzztalk.models.totalCount.request.UserID
import com.ssip.buzztalk.utils.Constants
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class UserDetailProfileFragment : Fragment() {

    private var _binding: FragmentUserDetailProfileBinding? = null
    private val binding get() = _binding!!
    private val args: UserDetailProfileFragmentArgs by navArgs()

    private val userDetailProfileViewModel: UserDetailProfileViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Toast.makeText(context, args.userId, Toast.LENGTH_LONG).show()

        userDetailProfileViewModel.getInfo(tokenManager.getTokenWithBearer()!!, OtherUserInfoRequest(args.userId))


        userDetailProfileViewModel.otherUserInfo.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    binding.fullName.text = "${response.data!!.data.user.firstName} ${response.data!!.data.user.lastName}"
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        binding.followUnfollow.setOnClickListener {
            if (binding.followUnfollow.text.toString().toLowerCase().trim() == "follow") {
                userDetailProfileViewModel.followUser(tokenManager.getTokenWithBearer()!!, Followee(args.userId))
            } else {
                userDetailProfileViewModel.unFollowUser(tokenManager.getTokenWithBearer()!!, Followee(args.userId))
            }
        }

        userDetailProfileViewModel.checkIfUserIsFollowedOrNot(tokenManager.getTokenWithBearer()!!, Followee(args.userId))

        userDetailProfileViewModel.userFollowedOrNot.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    Log.d("MESSAGE", "onViewCreated: ${response.data!!.message}")
                    if (response.data.message == Constants.USER_FOLLOWED) {
                        binding.followUnfollow.setBackgroundResource(R.drawable.btnsign_back)
                        binding.followUnfollow.text = "Unfollow"
                        binding.followUnfollow.setTextColor(resources.getColor(R.color.black))
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.data!!.message)
                }
            }
        }

        userDetailProfileViewModel.followUser.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    binding.followUnfollow.setBackgroundResource(R.drawable.btnsign_back)
                    binding.followUnfollow.text = "Unfollow"
                    binding.followUnfollow.setTextColor(resources.getColor(R.color.black))
                }
                Status.LOADING -> {
                    binding.followUnfollow.isEnabled = false
                }
                Status.ERROR -> {
                    binding.followUnfollow.isEnabled = true
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        userDetailProfileViewModel.unFollowUser.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    binding.followUnfollow.setBackgroundResource(R.drawable.btnlogin_back)
                    binding.followUnfollow.text = "Follow"
                    binding.followUnfollow.setTextColor(resources.getColor(R.color.white))
                }
                Status.LOADING -> {
                    binding.followUnfollow.isEnabled = false
                }
                Status.ERROR -> {
                    binding.followUnfollow.isEnabled = true
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        userDetailProfileViewModel.getFollowersFollowingCount(tokenManager.getTokenWithBearer()!!, UserID(args.userId))

        userDetailProfileViewModel.totalFollowersFollowingCount.observe(viewLifecycleOwner) { response ->
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
    }
}