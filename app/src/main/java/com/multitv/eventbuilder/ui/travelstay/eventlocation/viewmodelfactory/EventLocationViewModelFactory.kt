package com.multitv.eventbuilder.ui.travelstay.eventlocation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.travelstay.dodonot.viewmodel.DoDoNotViewModel
import com.multitv.eventbuilder.ui.travelstay.eventlocation.viewmodel.EventLocationViewModel

class EventLocationViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventLocationViewModel::class.java)) {
            return EventLocationViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}