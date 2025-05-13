package com.multitv.eventbuilder.ui.conference.msilContact.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.dresscode.viewmodel.DressViewModel
import com.multitv.eventbuilder.ui.conference.msilContact.viewmodel.MsilContactViewModel

class MsilContactViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MsilContactViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MsilContactViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel source")
    }
}