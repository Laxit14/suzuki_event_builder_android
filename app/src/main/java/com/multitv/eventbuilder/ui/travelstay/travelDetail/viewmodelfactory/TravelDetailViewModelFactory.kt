package com.multitv.eventbuilder.ui.travelstay.travelDetail.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.conference_detail.viewmodel.ConferenceAgendaViewModel
import com.multitv.eventbuilder.ui.travelstay.travelDetail.viewmodel.TravelDetailViewModel

class TravelDetailViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelDetailViewModel::class.java)) {
            return TravelDetailViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}