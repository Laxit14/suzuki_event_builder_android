package com.multitv.eventbuilder.ui.conference.seatingplan.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.seatingplan.viewmodel.SeatPlanViewModel
import com.multitv.eventbuilder.ui.travelstay.weather.viewmodel.WeatherViewModel

class SeatingPlanViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeatPlanViewModel::class.java)) {
            return SeatPlanViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}