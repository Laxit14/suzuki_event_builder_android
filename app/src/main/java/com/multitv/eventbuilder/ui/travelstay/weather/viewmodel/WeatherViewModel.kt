package com.multitv.eventbuilder.ui.travelstay.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.weathermodel.model.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel(private val repo: Repo) : ViewModel() {


    private val _weatherLiveData = MutableLiveData<Generic<WeatherResponse>>()
    val weatherLiveData: LiveData<Generic<WeatherResponse>> get() = _weatherLiveData

    fun fetchWeather(url : String) {
        viewModelScope.launch {
            _weatherLiveData.value = Generic.Loading
            try {
                val response = repo.getWetherData(url)
                _weatherLiveData.value = response
            } catch (e: Exception) {
                _weatherLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}