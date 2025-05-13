package com.multitv.eventbuilder.ui.travelstay.eventlocation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.eventlocation.model.EventLocationResponse
import kotlinx.coroutines.launch

class EventLocationViewModel( private val repo: Repo) : ViewModel() {

    private val _eventLocationLiveData = MutableLiveData<Generic<EventLocationResponse>>()
    val eventLocationLiveData: LiveData<Generic<EventLocationResponse>> get() = _eventLocationLiveData

    fun getEventResponseData(url : String) {
        viewModelScope.launch {
            _eventLocationLiveData.value = Generic.Loading
            try {
                val response = repo.getEventLocationResponse(url)
                _eventLocationLiveData.value = response
            } catch (e: Exception) {
                _eventLocationLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
}