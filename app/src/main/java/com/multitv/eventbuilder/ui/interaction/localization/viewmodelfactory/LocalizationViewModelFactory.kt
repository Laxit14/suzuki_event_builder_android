package com.multitv.eventbuilder.ui.interaction.localization.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.awards.viewmodel.AwardsViewModel
import com.multitv.eventbuilder.ui.interaction.localization.viewmodel.LocalizationViewModel

class LocalizationViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LocalizationViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LocalizationViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}