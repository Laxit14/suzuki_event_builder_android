package com.multitv.eventbuilder.ui.lamplight.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.conference_detail.viewmodel.ConferenceAgendaViewModel
import com.multitv.eventbuilder.ui.lamplight.viewmodel.LampLightinfViewModel

class LampLightingViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LampLightinfViewModel::class.java)) {
            return LampLightinfViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}