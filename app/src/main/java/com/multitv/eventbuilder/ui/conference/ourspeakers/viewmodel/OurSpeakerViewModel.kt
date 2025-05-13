package com.multitv.eventbuilder.ui.conference.ourspeakers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.ourspeaker.model.OurSpeakerResponse
import kotlinx.coroutines.launch

class OurSpeakerViewModel(private val repo: Repo) : ViewModel() {

    private val _ourSpeakerResponse = MutableLiveData<Generic<OurSpeakerResponse>>()
    val ourSpeakerResponse: LiveData<Generic<OurSpeakerResponse>> get() = _ourSpeakerResponse

    fun getSpeakerData(url : String){
        viewModelScope.launch {
            _ourSpeakerResponse.value = Generic.Loading
            try {
                val response = repo.getSpeakerData(url)
                _ourSpeakerResponse.value = response
            } catch (e: Exception) {
                _ourSpeakerResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
}