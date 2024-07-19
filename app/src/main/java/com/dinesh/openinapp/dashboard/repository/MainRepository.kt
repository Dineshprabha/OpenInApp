package com.dinesh.openinapp.dashboard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dinesh.openinapp.api.OpenInAppAPIService
import com.dinesh.openinapp.dashboard.model.DashboardResponse
import com.dinesh.openinapp.utils.NetworkResult
import javax.inject.Inject

class MainRepository @Inject constructor(private val openInAppAPIService: OpenInAppAPIService) {

    private val _userClicksLiveData = MutableLiveData<NetworkResult<DashboardResponse>>()
    val userClickData: LiveData<NetworkResult<DashboardResponse>>
        get() = _userClicksLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<Pair<Boolean, String>>>()
    val statusLiveData get() = _statusLiveData

    suspend fun getDashboardData() {
        _userClicksLiveData.postValue(NetworkResult.Loading())
        val response = openInAppAPIService.getDashboardData()

        if (response.isSuccessful && response.body() != null) {
            _userClicksLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
//            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//            _userClicksLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userClicksLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}