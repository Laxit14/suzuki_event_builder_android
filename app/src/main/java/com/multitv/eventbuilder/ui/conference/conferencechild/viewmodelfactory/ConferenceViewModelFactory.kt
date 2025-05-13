package com.multitv.eventbuilder.ui.conference.conferencechild.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.conferencechild.viewmodel.ConferenceViewModel
import com.multitv.eventbuilder.ui.interaction.askyouquestion.viewmodel.AskQuestionViewModel

class ConferenceViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ConferenceViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ConferenceViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}