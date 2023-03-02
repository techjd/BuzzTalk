package com.ssip.buzztalk.ui.fragments.newgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentNewGroupBinding

class NewGroupFragment : Fragment() {

  private var _binding: FragmentNewGroupBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentNewGroupBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    binding.newGrpDetailScreen.setOnClickListener {
      findNavController().navigate(R.id.action_newGroupFragment_to_newGroupDetailFragment)
    }
  }
}