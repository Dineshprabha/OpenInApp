package com.dinesh.openinapp.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinesh.openinapp.dashboard.model.DashboardResponse
import com.dinesh.openinapp.dashboard.repository.MainRepository
import com.dinesh.openinapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _data = MutableLiveData<NetworkResult<DashboardResponse>>()
    val data: LiveData<NetworkResult<DashboardResponse>> get() = _data

    fun fetchData() {
        viewModelScope.launch {
            _data.value = NetworkResult.Loading()
            _data.value = repository.getData()
        }
    }
}