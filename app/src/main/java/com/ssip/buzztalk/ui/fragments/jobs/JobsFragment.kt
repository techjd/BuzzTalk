package com.ssip.buzztalk.ui.fragments.jobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentJobsBinding
import com.ssip.buzztalk.databinding.FragmentNotificationsBinding
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobsFragment : Fragment() {

    private var _binding: FragmentJobsBinding? = null
    private val binding get() = _binding!!
    private val jobsViewModel: JobsViewModel by viewModels()
    private lateinit var jobsAdapter: JobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        jobsViewModel.getAllOppo()

        with(binding) {
            oppoRv.layoutManager = LinearLayoutManager(requireContext())
            oppoRv.setHasFixedSize(true)
        }

        jobsViewModel.allOpportunities.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                SUCCESS -> {
                    jobsAdapter = JobsAdapter()
                    jobsAdapter.opportunities = response.data?.data?.opportunities!!
                    binding.oppoRv.adapter = jobsAdapter
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

    }
}