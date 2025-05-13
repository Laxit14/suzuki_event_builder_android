package com.multitv.eventbuilder.ui.conference.seatingplan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.seatingplanmodel.model.SeatResponse
import kotlinx.coroutines.launch

class SeatPlanViewModel(private val repo: Repo) : ViewModel() {

    private val _seatingPlanResponse = MutableLiveData<Generic<SeatResponse>>()
    val seatPlanResponse: LiveData<Generic<SeatResponse>> get() = _seatingPlanResponse

    fun getSeatNo(url : String,userId : String) {
        viewModelScope.launch {
            _seatingPlanResponse.value = Generic.Loading
            try {
                val response = repo.getSeatResponse(url,userId)
                _seatingPlanResponse.value = response
            } catch (e: Exception) {
                _seatingPlanResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}