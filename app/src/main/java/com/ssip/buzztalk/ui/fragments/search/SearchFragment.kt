package com.ssip.buzztalk.ui.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentProfileBinding
import com.ssip.buzztalk.databinding.FragmentSearchBinding
import com.ssip.buzztalk.models.searchusers.response.User
import com.ssip.buzztalk.ui.fragments.search.adapter.SearchAdapter
import com.ssip.buzztalk.ui.fragments.search.viewmodel.SearchViewModel
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.getAllSearchUsers()
        binding.personsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.personsRecyclerView.setHasFixedSize(true)

        searchViewModel.getAllSearchUsers()

        searchViewModel.allSearchUsers.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    searchAdapter = SearchAdapter(glide, navigate = { userID ->
                        navigate(userID)
                    })
                    binding.personsRecyclerView.adapter = searchAdapter
                    binding.edtSearch.visibility = View.VISIBLE
                    searchAdapter.users = response.data!!.data.users as MutableList<User>
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }
    }

    private fun navigate(userId: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToUserDetailProfileFragment(userId)
        findNavController().navigate(action)
    }
}