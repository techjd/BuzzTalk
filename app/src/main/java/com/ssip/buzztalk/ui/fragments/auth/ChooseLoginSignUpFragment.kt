package com.ssip.buzztalk.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentChooseLoginSignUpBinding
import com.ssip.buzztalk.databinding.FragmentLoginBinding

class ChooseLoginSignUpFragment : Fragment() {

    private var _binding: FragmentChooseLoginSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseLoginSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_chooseLoginSignUpFragment_to_signUpFragment)
        }

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_chooseLoginSignUpFragment_to_loginFragment)
        }
    }
}