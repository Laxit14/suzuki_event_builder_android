package com.multitv.eventbuilder.ui.conference.emergencyContact.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.conference.emergencyContact.viewmodel.EmergencyContactViewmodel

class EmergencyContactViewmodelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EmergencyContactViewmodel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return EmergencyContactViewmodel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel source")
    }
}