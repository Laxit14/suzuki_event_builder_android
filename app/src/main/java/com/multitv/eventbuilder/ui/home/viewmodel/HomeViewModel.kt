package com.multitv.eventbuilder.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.model.ResponseNotification
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.homemodel.model.HomeDataItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: Repo) : ViewModel() {
    private val _homeDataResponse = MutableLiveData<Generic<HomeDataItem>>()
    val homeDataResponse: LiveData<Generic<HomeDataItem>> get() = _homeDataResponse
    private val _notificationDataResponse = MutableLiveData<Generic<ResponseNotification>>()
    val notificationDataResponse: LiveData<Generic<ResponseNotification>> get() = _notificationDataResponse
    fun getHomeData(url : String){
        viewModelScope.launch {
            _homeDataResponse.value = Generic.Loading
            try {
                val response = repo.getHomeData(url)
                _homeDataResponse.value = response
            } catch (e: Exception) {
                _homeDataResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
    fun getNotificationData(url : String){
        viewModelScope.launch {
            _homeDataResponse.value = Generic.Loading
            try {
                val response = repo.getNotificationResponse(url)
                _notificationDataResponse.value = response
            } catch (e: Exception) {
                _notificationDataResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun hasData(): Boolean {
        return _homeDataResponse.value is Generic.Success
    }
}