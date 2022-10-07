package com.ssip.buzztalk.ui.fragments.auth.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddEmailBinding
import com.ssip.buzztalk.databinding.FragmentAddFirstLastNameBinding

class AddEmailFragment : Fragment() {

    private var _binding: FragmentAddEmailBinding? = null
    private val binding get() = _binding!!
    val userSignUpViewModel: UserSignUpViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSignUpViewModel.email.observe(viewLifecycleOwner) {
            binding.emailAddress.setText(it)
        }

        userSignUpViewModel.userName.observe(viewLifecycleOwner) {
            binding.userName.setText(it)
        }

        binding.next.setOnClickListener {
            val email = binding.emailAddress.text.toString().trim()
            val userName = binding.userName.text.toString().trim()


            if (validateText(email, userName)) {
                if (validateEmail(email)) {
                    userSignUpViewModel.saveEmail(email)
                    userSignUpViewModel.saveUserName(userName)
                    findNavController().navigate(R.id.action_addEmailFragment_to_addPasswordFragment)
                } else {
                    Toast.makeText(context, "Not a Valid Email Address", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Please Fill Email Address", Toast.LENGTH_LONG).show()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_addEmailFragment_to_addFirstLastNameFragment)
            }
        })
    }

    private fun validateText(inputEmail: String, inputUserName: String): Boolean {
        if (inputEmail.isNotEmpty() && inputUserName.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun validateEmail(email: String): Boolean {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }
        return false
    }

}