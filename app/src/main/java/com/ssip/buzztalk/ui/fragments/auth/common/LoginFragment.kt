package com.ssip.buzztalk.ui.fragments.auth.common

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentLoginBinding
import com.ssip.buzztalk.models.auth.user.login.request.UserRequestLogin
import com.ssip.buzztalk.ui.activities.CompanyActivity
import com.ssip.buzztalk.ui.activities.MainActivity
// import com.ssip.buzztalk.ui.activities.UniversityActivity
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val signInViewModel: SignInViewModel by viewModels()
    private val args: LoginFragmentArgs by navArgs()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            // 0 means users
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if (args.userType == 0) {
                if (validateEmail(email) && validatePass(password)) {
                    signInViewModel.login(
                        UserRequestLogin(
                            email = email,
                            password = password
                        )
                    )
                }
                // 1 means university
            } else if(args.userType == 1) {
                if (validateEmail(email) && validatePass(password)) {
                    signInViewModel.loginUniversity(
                        UserUniversityRequest(
                            universityEmail = email,
                            universityPassword = password
                        )
                    )
                }
            } else if(args.userType == 2) {
                if (validateEmail(email) && validatePass(password)) {
                    signInViewModel.loginCompany(
                        UserCompanyRequest(
                            companyEmail = email,
                            companyPassword = password
                        )
                    )
                }
            }
        }

        signInViewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    tokenManager.saveToken(response.data!!.data.token)
                    tokenManager.saveUserId(response.data!!.data.user._id)
                    tokenManager.saveUserType(response.data!!.data.user.userType)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    (activity as MainActivity).setStartDestinationAsHomeFragment()
                }
                Status.LOADING -> {
                    showProgressBar()
                }
                Status.ERROR -> {
                    hideProgressBar()
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

//         signInViewModel.loginResponseCompany.observe(viewLifecycleOwner) { response ->
//             when (response.status) {
//                 Status.SUCCESS -> {
//                     hideProgressBar()
//                     tokenManager.saveToken(response.data!!.data.token)
//                     tokenManager.saveUserId(response.data!!.data.user._id)
//                     tokenManager.saveUserType("COMPANY")
// //                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
// //                    (activity as MainActivity).setStartDestinationAsHomeFragment()
//
//                     Intent(context,  CompanyActivity::class.java).apply {
//                         startActivity(this)
//                     }
//                     (activity as MainActivity).finish()
//                 }
//                 Status.LOADING -> {
//                     showProgressBar()
//                 }
//                 Status.ERROR -> {
//                     hideProgressBar()
//                     DialogClass(view).showDialog(response.message!!)
//                 }
//             }
//         }

//         signInViewModel.loginResponseUniversity.observe(viewLifecycleOwner) { response ->
//             when (response.status) {
//                 Status.SUCCESS -> {
//                     hideProgressBar()
//                     tokenManager.saveToken(response.data!!.data.token)
//                     tokenManager.saveUserId(response.data!!.data.user._id)
//                     tokenManager.saveUserType("UNIVERSITY")
//
//                     Intent(context,  UniversityActivity::class.java).apply {
//                         startActivity(this)
//                     }
//                     (activity as MainActivity).finish()
//
// //                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
// //                    (activity as MainActivity).setStartDestinationAsHomeFragment()
//                 }
//                 Status.LOADING -> {
//                     showProgressBar()
//                 }
//                 Status.ERROR -> {
//                     hideProgressBar()
//                     DialogClass(view).showDialog(response.message!!)
//                 }
//             }
//         }



        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_loginFragment_to_chooseLoginSignUpFragment2)
                }
            })

    }

    private fun validatePass(password: String): Boolean {
        if (password.isNotEmpty()) {
            return true
        }
        _binding?.password?.setError("Password not valid.")
        return false
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }
        _binding?.emailAddress?.setError("Enter valid emial address.")
        return false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
}