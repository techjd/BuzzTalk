package com.ssip.buzztalk.ui.fragments.auth.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddCompanyNameBinding

class AddCompanyNameFragment : Fragment() {

    private var _binding: FragmentAddCompanyNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCompanyNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.next.setOnClickListener {
            val com_full_name = binding.comFullName.toString().trim()
            val com_uname = binding.comUname.toString().trim()
            val com_motto = binding.comMotto.toString().trim()

            if (validateF(com_full_name) && validateU(com_uname) && validateM(com_motto)) {
                findNavController().navigate(R.id.action_addCompanyNameFragment_to_addCompanyLinkFragment)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_addCompanyNameFragment_to_chooseLoginSignUpFragment)
            }
        })
    }

    private fun validateF(Fname: String): Boolean {
        if (Fname.isNotEmpty()) {
            return true
        }
        _binding?.comFullName?.setError("Company name not valid.")
        return false
    }

    private fun validateU(Sname: String): Boolean {
        if (Sname.isNotEmpty()) {
            return true
        }
        _binding?.comUname?.setError("User name not valid.")
        return false
    }

    private fun validateM(Sname: String): Boolean {
        if (Sname.isNotEmpty()) {
            return true
        }
        _binding?.comMotto?.setError("User name not valid.")
        return false
    }

}