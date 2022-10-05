package com.ssip.buzztalk.ui.fragments.auth.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddCompanyLinksBinding
import com.ssip.buzztalk.databinding.FragmentAddEmailBinding
import com.ssip.buzztalk.ui.fragments.auth.user.UserSignUpViewModel

class AddCompanyLinks : Fragment() {

    private var _binding: FragmentAddCompanyLinksBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCompanyLinksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}