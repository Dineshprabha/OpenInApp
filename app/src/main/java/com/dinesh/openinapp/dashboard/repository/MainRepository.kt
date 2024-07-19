package com.dinesh.openinapp.dashboard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dinesh.openinapp.api.OpenInAppAPIService
import com.dinesh.openinapp.dashboard.model.DashboardResponse
import com.dinesh.openinapp.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class MainRepository @Inject constructor(private val openInAppAPIService: OpenInAppAPIService) {

    suspend fun getData(): NetworkResult<DashboardResponse> {
        return try {
            val response = openInAppAPIService.getDashboardData()
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Error: ${response.code()}", null)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message, null)
        }
    }
}