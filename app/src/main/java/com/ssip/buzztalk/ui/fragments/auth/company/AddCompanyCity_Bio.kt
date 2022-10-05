package com.ssip.buzztalk.ui.fragments.auth.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddCompanyCityBioBinding
import com.ssip.buzztalk.databinding.FragmentAddCompanyLinksBinding

class AddCompanyCity_Bio : Fragment() {

    private var _binding: FragmentAddCompanyCityBioBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddCompanyCityBioBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}