package com.ssip.buzztalk.ui.fragments.search.userdetailprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentSearchBinding
import com.ssip.buzztalk.databinding.FragmentUserDetailProfileBinding


class UserDetailProfileFragment : Fragment() {

    private var _binding: FragmentUserDetailProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}