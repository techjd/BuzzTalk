package com.ssip.buzztalk.ui.fragments.auth.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentLoginBinding
import com.ssip.buzztalk.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val args: SignUpFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.StudentCard.setOnClickListener {
            if (args.loginSignup == 0) {
                // 0 for student
                val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment(0)
                findNavController().navigate(action)
            } else {
                findNavController().navigate(R.id.action_signUpFragment_to_addFirstLastNameFragment)
            }
        }
        binding.UniversityCard.setOnClickListener {
            if (args.loginSignup == 0) {
                // 1 for university
                val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment(1)
                findNavController().navigate(action)
            } else {
                findNavController().navigate(R.id.action_signUpFragment_to_addFirstLastNameFragment)
            }        }
        binding.industryCard.setOnClickListener {
            if (args.loginSignup == 0) {
                // 2 for company
                val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment(2)
                findNavController().navigate(action)
            } else {
                findNavController().navigate(R.id.action_signUpFragment_to_addFirstLastNameFragment)
            }
        }

//        binding.schoolCard.setOnClickListener {
//
//        }
//
//        binding.UniversityCard.setOnClickListener {
//
//        }
//
//        binding.industryCard.setOnClickListener {
//
//        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_signUpFragment_to_chooseLoginSignUpFragment2)
            }
        })

    }

}