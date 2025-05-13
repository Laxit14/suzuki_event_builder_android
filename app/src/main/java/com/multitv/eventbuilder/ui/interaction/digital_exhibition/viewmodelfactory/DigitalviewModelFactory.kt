package com.multitv.eventbuilder.ui.interaction.digital_exhibition.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.interaction.digital_exhibition.viewmodel.DigitalExhibitionViewModel
import com.multitv.eventbuilder.ui.interaction.interactionchild.viewmodel.InteractionViewModel

class DigitalviewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DigitalExhibitionViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DigitalExhibitionViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}