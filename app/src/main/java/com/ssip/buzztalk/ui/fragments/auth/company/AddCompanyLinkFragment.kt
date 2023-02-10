package com.ssip.buzztalk.ui.fragments.auth.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddCompanyLinkBinding

class AddCompanyLinkFragment : Fragment() {

    private var _binding: FragmentAddCompanyLinkBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCompanyLinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.next.setOnClickListener {
            val CompanyWebLink = binding.CompanyWebLink.toString().trim()
            val CompanyGeoLink = binding.CompanyGeoLink.toString().trim()
            val categorySpinner = binding.categorySpinner.toString().trim()

            if (validateWB(CompanyWebLink) && validateCSP(categorySpinner)) {
                findNavController().navigate(R.id.action_addCompanyNameFragment_to_addCompanyLinkFragment)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_addCompanyNameFragment_to_chooseLoginSignUpFragment)
                }
            })
    }

    private fun validateWB(Fname: String): Boolean {
        if (Fname.isNotEmpty()) {
            return true
        }
        _binding?.CompanyWebLink?.setError("Company name not valid.")
        return false
    }

    private fun validateCSP(Sname: String): Boolean {
        if (Sname.isNotEmpty()) {
            return true
        }
        Toast.makeText(requireContext(), "Please select company type.", Toast.LENGTH_SHORT).show()
        return false
    }
}