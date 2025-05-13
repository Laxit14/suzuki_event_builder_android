package com.multitv.eventbuilder.ui.interaction.entertainment.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.interaction.digitalexhibitionfeedback.viewmodel.DigitalExhibitionFeedbackViewModel
import com.multitv.eventbuilder.ui.interaction.entertainment.viewmodel.EntertainmentViewModel

class EntertainmentViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EntertainmentViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return EntertainmentViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}