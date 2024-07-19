package com.dinesh.openinapp.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinesh.openinapp.dashboard.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val userClickLiveData get() = repository.userClickData

    fun fetchData(){
        viewModelScope.launch {
            repository.getDashboardData()
        }
    }
}