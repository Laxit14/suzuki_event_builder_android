package com.multitv.eventbuilder.ui.conference.conference_detail.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.conference_detail.viewmodel.ConferenceAgendaViewModel
import com.multitv.eventbuilder.ui.conference.seatingplan.viewmodel.SeatPlanViewModel

class ConferenceAgendaViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConferenceAgendaViewModel::class.java)) {
            return ConferenceAgendaViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}