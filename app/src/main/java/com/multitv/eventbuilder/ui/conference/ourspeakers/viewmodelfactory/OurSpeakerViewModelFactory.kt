package com.multitv.eventbuilder.ui.conference.ourspeakers.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.ourspeakers.viewmodel.OurSpeakerViewModel
import com.multitv.eventbuilder.ui.interaction.currencyconvertor.viewmodel.CurrencyConvertorViewModel

class OurSpeakerViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OurSpeakerViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OurSpeakerViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }

}