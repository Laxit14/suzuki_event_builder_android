package com.multitv.eventbuilder.ui.travelstay.dodonot.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.travelstay.dodonot.viewmodel.DoDoNotViewModel
import com.multitv.eventbuilder.ui.travelstay.weather.viewmodel.WeatherViewModel

class DoDoNotViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoDoNotViewModel::class.java)) {
            return DoDoNotViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}