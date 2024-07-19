package com.dinesh.openinapp.dashboard.ui.fragments

import android.graphics.Color
import android.icu.util.Calendar
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
import com.dinesh.openinapp.R
import com.dinesh.openinapp.dashboard.adapter.DashboardItemAdapter
import com.dinesh.openinapp.dashboard.adapter.TabLayoutAdapter
import com.dinesh.openinapp.dashboard.model.DashboardItem
import com.dinesh.openinapp.dashboard.model.DashboardResponse
import com.dinesh.openinapp.dashboard.model.OverallUrlChart
import com.dinesh.openinapp.dashboard.ui.fragments.home.RecentLinkFragment
import com.dinesh.openinapp.dashboard.ui.fragments.home.TopLinkFragment
import com.dinesh.openinapp.dashboard.viewmodel.MainViewModel
import com.dinesh.openinapp.databinding.FragmentHomeBinding
import com.dinesh.openinapp.utils.AppConstants.BEARER_TOKEN
import com.dinesh.openinapp.utils.DateAxisValueFormatter
import com.dinesh.openinapp.utils.NetworkResult
import com.dinesh.openinapp.utils.TokenManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<MainViewModel>()
    private val dashboardItemAdapter by lazy { DashboardItemAdapter() }
    private val dashboardItemList = ArrayList<DashboardItem>()
    private lateinit var chartData: Map<String, Int>

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager.saveToken(BEARER_TOKEN)

        binding.tvGreetings.text = getGreetingMessage()

        viewModel.fetchData()
        setupDashboardItemRV()


        val categoriesFragment = arrayListOf(
            TopLinkFragment(),
            RecentLinkFragment()
        )

        binding.viewPager2.isUserInputEnabled = false
        val viewPagerAdapter = TabLayoutAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Top Links"
                1 -> tab.text = "Recent Links"
            }
        }.attach()
        setupObserver()
    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        return when {
            hourOfDay < 12 -> "Good Morning!"
            hourOfDay < 18 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
    }


    private fun setupDashboardItemRV() {
        binding.dashboardRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = dashboardItemAdapter
        }
    }

    private fun setupObserver() {
        viewModel.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        val dashboardResponse: DashboardResponse = it.data
                        setupDashboardItemData(dashboardResponse)
                        val overallUrlChart: OverallUrlChart =
                            dashboardResponse.data.overall_url_chart
//                        chartData = extractChartData(overallUrlChart)
//                        populatelineChart(chartData)
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

    private fun populatelineChart(chartData: Map<String, Int>) {

        // Extract data from your JSON-like structure
        val data = chartData

        // Create entries for the chart
        val entries = ArrayList<Entry>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        for ((date, value) in data) {
            calendar.time = dateFormat.parse(date)!!

            // Convert date to timestamp for X-axis
            val timestamp = calendar.timeInMillis.toFloat()
            entries.add(Entry(timestamp, value.toFloat()))
        }

        // Create a dataset with entries
        val dataSet = LineDataSet(entries, "Overall URL Chart")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK

        // Create a LineData object with the dataset
        val lineData = LineData(dataSet)

        val lineChart = binding.lineChart
        // Set up X-axis with date labels
        val xAxis: XAxis = lineChart.xAxis
        xAxis.valueFormatter = DateAxisValueFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -45f

        // Set up chart properties
        lineChart.data = lineData
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.invalidate()

    }


    fun extractChartData(overallUrlChart: OverallUrlChart): Map<String, Int> {
        val properties = OverallUrlChart::class.java.declaredFields
        return properties.associate {
            it.isAccessible = true
            it.name to it.getInt(overallUrlChart)
        }
    }

    private fun setupDashboardItemData(userClickResponse: DashboardResponse) {
        dashboardItemList.add(
            DashboardItem(
                R.drawable.avatar_two,
                userClickResponse.today_clicks.toString(),
                "Today's Clicks"
            )
        )
        dashboardItemList.add(
            DashboardItem(
                R.drawable.avathar_one,
                userClickResponse.top_location.toString(),
                "Top Location"
            )
        )
        dashboardItemList.add(
            DashboardItem(
                R.drawable.avatar_three,
                userClickResponse.top_source.toString(),
                "Top Source"
            )
        )
        dashboardItemList.add(
            DashboardItem(
                R.drawable.avathar_four,
                userClickResponse.startTime.toString(),
                "Best Time"
            )
        )
        dashboardItemAdapter.differ.submitList(dashboardItemList)
    }
}