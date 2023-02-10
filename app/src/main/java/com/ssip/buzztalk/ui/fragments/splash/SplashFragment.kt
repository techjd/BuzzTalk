package com.ssip.buzztalk.ui.fragments.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentSplashBinding
import com.ssip.buzztalk.ui.activities.MainActivity
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            if (tokenManager.getToken() != null) {
                delay(1600L)
                val userType = tokenManager.getUserType()
                if (userType == "INDUSTRY" || userType == "SCHOOL" || userType == "UNIVERSITY") {
                    findNavController().navigate(R.id.action_splashFragment_to_companyActivity)
                    (activity as MainActivity).finish()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    (activity as MainActivity).setStartDestinationAsHomeFragment()
                }
            } else {
                delay(1500L)
                findNavController().navigate(R.id.action_splashFragment_to_chooseLoginSignUpFragment)
                (activity as MainActivity).setStartDestinationAsSplashFragment()
            }
        }
    }
}