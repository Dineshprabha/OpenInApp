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
import com.dinesh.openinapp.dashboard.adapter.RecentLinkAdapter
import com.dinesh.openinapp.dashboard.model.DashboardResponse
import com.dinesh.openinapp.dashboard.viewmodel.MainViewModel
import com.dinesh.openinapp.databinding.FragmentRecentLinkBinding
import com.dinesh.openinapp.utils.AppConstants
import com.dinesh.openinapp.utils.NetworkResult
import com.dinesh.openinapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecentLinkFragment : Fragment() {

    private lateinit var binding: FragmentRecentLinkBinding
    private val viewModel by viewModels<MainViewModel>()
    private val recentLinkAdapter by lazy { RecentLinkAdapter() }

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecentLinkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager.saveToken(AppConstants.BEARER_TOKEN)
        viewModel.fetchData()
        setupTopLinkRV()
        setupObserver()
    }

    private fun setupTopLinkRV() {
        binding.rvRecentlinks.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = recentLinkAdapter
        }
    }

    private fun setupObserver() {
        viewModel.userClickLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        val userClickResponse: DashboardResponse = it.data
                        recentLinkAdapter.differ.submitList(userClickResponse.data.recent_links)
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