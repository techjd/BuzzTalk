package com.ssip.buzztalk.ui.fragments.userdetailprofile

import android.os.Bundle
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

        Toast.makeText(context, args.userId, Toast.LENGTH_LONG).show()

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

        binding.follow.setOnClickListener {
            userDetailProfileViewModel.followUser(tokenManager.getTokenWithBearer()!!, Followee(args.userId))
        }

        userDetailProfileViewModel.followUser.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    binding.follow.setBackgroundResource(R.drawable.btnsign_back)
                    binding.follow.text = "Unfollow"
                    binding.follow.setTextColor(resources.getColor(R.color.white))
                }
                Status.LOADING -> {
                    binding.follow.isEnabled = false
                }
                Status.ERROR -> {
                    binding.follow.isEnabled = true
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }
    }
}