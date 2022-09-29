package com.ssip.buzztalk.ui.fragments.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentHomeBinding
import com.ssip.buzztalk.databinding.FragmentPostBinding


class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.postToolBar.topPostAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.postToolBar.topPostAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.postMenu -> {
                    Toast.makeText(context, "Post the content", Toast.LENGTH_LONG).show()
//                    findNavController().navigate(R.id.action_homeFragment_to_chatsFragment)
                    true
                }
                else -> false
            }
        }
    }
}