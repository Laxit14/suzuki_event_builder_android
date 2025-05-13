package com.multitv.eventbuilder.ui.travelstay.weather.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.travelstay.weather.viewmodel.WeatherViewModel

class WeatherViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}