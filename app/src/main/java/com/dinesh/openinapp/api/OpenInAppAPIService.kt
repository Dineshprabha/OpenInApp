package com.dinesh.openinapp.api

import com.dinesh.openinapp.dashboard.model.DashboardResponse
import retrofit2.Response
import retrofit2.http.GET

interface OpenInAppAPIService {

    @GET("dashboardNew")
    suspend fun getDashboardData(): Response<DashboardResponse>
}