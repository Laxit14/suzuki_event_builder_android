package com.multitv.eventbuilder.ui.travelstay.travelstayContent.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.home.viewmodel.HomeViewModel
import com.multitv.eventbuilder.ui.travelstay.travelstayContent.viewmodel.TravelAndStayViewModel

class TravelAndStayViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TravelAndStayViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return TravelAndStayViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}