package com.multitv.eventbuilder.ui.conference.emergencyContact.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.model.emergencymodel.EmergencyResponse
import com.multitv.eventbuilder.model.msilcontact.model.MsilContactResponse
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.conference.msilContact.viewmodel.MsilContactViewModel
import kotlinx.coroutines.launch

class EmergencyContactViewmodel(private val repo: Repo) : ViewModel() {

    private val _emergencyContact = MutableLiveData<Generic<EmergencyResponse>>()
    val emergencyContact: LiveData<Generic<EmergencyResponse>> get() = _emergencyContact

    fun emergencyContactData(url : String) {
        viewModelScope.launch {
            _emergencyContact.value = Generic.Loading
            try {
                val response = repo.getEmergencyResponse(url)
                _emergencyContact.value = response
            } catch (e: Exception) {
                _emergencyContact.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}