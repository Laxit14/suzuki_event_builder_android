package com.multitv.eventbuilder.ui.conference.awards.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.awards.viewmodel.AwardsViewModel
import com.multitv.eventbuilder.ui.conference.conferencechild.viewmodel.ConferenceViewModel

class AwardsViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AwardsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AwardsViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}