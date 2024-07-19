package com.dinesh.openinapp.dashboard.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinesh.openinapp.dashboard.adapter.TopLinkAdapter
import com.dinesh.openinapp.dashboard.model.DashboardResponse
import com.dinesh.openinapp.dashboard.viewmodel.MainViewModel
import com.dinesh.openinapp.databinding.FragmentTopLinkBinding
import com.dinesh.openinapp.utils.AppConstants
import com.dinesh.openinapp.utils.NetworkResult
import com.dinesh.openinapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopLinkFragment : Fragment() {

    private lateinit var binding: FragmentTopLinkBinding
    private val viewModel by viewModels<MainViewModel>()
    private val linkAdapter by lazy { TopLinkAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTopLinkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchData()
        setupTopLinkRV()
        setupObserver()
    }

    private fun setupTopLinkRV() {
        binding.rvToplinks.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = linkAdapter
        }
    }

    private fun setupObserver() {
        viewModel.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        val dashboardResponse: DashboardResponse = it.data
                        linkAdapter.differ.submitList(dashboardResponse.data.top_links)
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Loading -> {

                }

                else -> Unit
            }
        })

    }
}