package com.multitv.eventbuilder.ui.conference.dresscode.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.dresscode.viewmodel.DressViewModel

class DressViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DressViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DressViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel source")
    }
}