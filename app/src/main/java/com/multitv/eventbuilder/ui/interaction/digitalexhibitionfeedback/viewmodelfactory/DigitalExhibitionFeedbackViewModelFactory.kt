package com.multitv.eventbuilder.ui.interaction.digitalexhibitionfeedback.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.conferencechild.viewmodel.ConferenceViewModel
import com.multitv.eventbuilder.ui.interaction.digitalexhibitionfeedback.viewmodel.DigitalExhibitionFeedbackViewModel

class DigitalExhibitionFeedbackViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DigitalExhibitionFeedbackViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DigitalExhibitionFeedbackViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}