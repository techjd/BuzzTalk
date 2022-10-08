package com.ssip.buzztalk.ui.fragments.auth.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddFirstLastNameBinding
import com.ssip.buzztalk.databinding.FragmentLoginBinding

class AddFirstLastNameFragment : Fragment() {

    private var _binding: FragmentAddFirstLastNameBinding? = null
    private val binding get() = _binding!!
    private val userSignUpViewModel: UserSignUpViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFirstLastNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSignUpViewModel.firstName.observe(viewLifecycleOwner) {
            binding.firstName.setText(it)
        }

        userSignUpViewModel.secondName.observe(viewLifecycleOwner) {
            binding.secondName.setText(it)
        }

        binding.next.setOnClickListener {
            val firstName = binding.firstName.text.toString().trim()
            val secondName = binding.secondName.text.toString().trim()

            if (validateFname(firstName)&&validateSname(secondName)) {
                userSignUpViewModel.saveFirstNameAndSecond(firstName, secondName)
                findNavController().navigate(R.id.action_addFirstLastNameFragment_to_addEmailFragment)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_addFirstLastNameFragment_to_chooseLoginSignUpFragment)
            }
        })
    }

    private fun validateFname(Fname: String): Boolean {
        if (Fname.isNotEmpty()) {
            return true
        }
        _binding?.firstName?.setError("First name not valid.")
        return false
    }

    private fun validateSname(Sname: String): Boolean {
        if (Sname.isNotEmpty()) {
            return true
        }
        _binding?.secondName?.setError("Second name not valid.")
        return false
    }

}