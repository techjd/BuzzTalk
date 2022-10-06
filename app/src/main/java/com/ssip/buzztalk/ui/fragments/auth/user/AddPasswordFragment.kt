package com.ssip.buzztalk.ui.fragments.auth.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddPasswordBinding
import com.ssip.buzztalk.models.auth.user.register.request.UserRequestRegister
import com.ssip.buzztalk.ui.activities.MainActivity
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPasswordFragment : Fragment() {

    private var _binding: FragmentAddPasswordBinding? = null
    private val binding get() = _binding!!
    private val userSignUpViewModel: UserSignUpViewModel by activityViewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userSignUpViewModel.email.observe(viewLifecycleOwner) {
            binding.emailAddress.setText(it)
        }

        userSignUpViewModel.password.observe(viewLifecycleOwner) {
            binding.password.setText(it)
        }

        userSignUpViewModel.registerResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    tokenManager.saveToken(response.data!!.data.token)
                    tokenManager.saveUserId(response.data!!.data.user._id)
                    findNavController().navigate(R.id.action_addPasswordFragment_to_homeFragment)
                    (activity as MainActivity).setStartDestinationAsHomeFragment()
                }
                Status.ERROR -> {
                    hideProgressBar()
                    DialogClass(view).showDialog(response.message!!)
                }
                Status.LOADING -> {
                    showProgressBar()
                }
            }
        }

        binding.agreeAndJoin.setOnClickListener {
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val firstName = userSignUpViewModel.firstName.value.toString().trim()
            val lastName = userSignUpViewModel.secondName.value.toString().trim()

            if (validateDetails(email, password)) {
                userSignUpViewModel.register(
                    UserRequestRegister(
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName
                    )
                )
            } else if (password.length < 6) {
                Toast.makeText(context, "Password is not 6 Characters Long", Toast.LENGTH_LONG)
                    .show()
            }
            else
                Toast.makeText(context, "Email is not valid", Toast.LENGTH_LONG)
                    .show()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    userSignUpViewModel.saveEmail(binding.emailAddress.text.toString().trim())
                    userSignUpViewModel.savePassword(binding.password.text.toString().trim())
                    findNavController().navigate(R.id.action_addPasswordFragment_to_addEmailFragment)
                }
            })
    }

    private fun validateDetails(email: String, password: String): Boolean {
        if (password.isNotEmpty() && password.length >= 6 && email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(
                email
            ).matches()
        ) {
            return true
        }
        return false
    }

    private fun showProgressBar() {
        binding.registerProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.registerProgressBar.visibility = View.INVISIBLE
    }

}