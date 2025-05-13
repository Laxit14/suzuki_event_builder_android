package com.multitv.eventbuilder.ui.interaction.entertainment.viewmodel

import EntertainmentResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.model.digitalexhibitionfeedbackmodel.model.DigitalExhibitionFeedbackResponse
import com.multitv.eventbuilder.sealed.Generic
import kotlinx.coroutines.launch

class EntertainmentViewModel(private val repo: Repo): ViewModel() {

    private val _entertainmentLiveData = MutableLiveData<Generic<EntertainmentResponse>>()
    val entertainmentLiveData: LiveData<Generic<EntertainmentResponse>> get() = _entertainmentLiveData

    fun getEntertainmentData(url : String) {
        viewModelScope.launch {
            _entertainmentLiveData.value = Generic.Loading
            try {
                val response = repo.getEntertainment(url)
                _entertainmentLiveData.value = response
            } catch (e: Exception) {
                _entertainmentLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }


}