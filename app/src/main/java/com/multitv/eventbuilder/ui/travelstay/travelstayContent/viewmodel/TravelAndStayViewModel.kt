package com.multitv.eventbuilder.ui.travelstay.travelstayContent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.model.travelstay.model.TravelAndStayResponse
import com.multitv.eventbuilder.sealed.Generic
import kotlinx.coroutines.launch

class TravelAndStayViewModel(private val repo: Repo) : ViewModel() {

    private var _travelAndStayResponse = MutableLiveData<Generic<TravelAndStayResponse>>()
    val travelAndStayResponse: LiveData<Generic<TravelAndStayResponse>> get() = _travelAndStayResponse

    private var fetchedHotelStayId: Int? = null

    fun getTravelAndStayData(hotelStayId: Int, category: String? = null) {

        if (fetchedHotelStayId == hotelStayId) return
        viewModelScope.launch {
            _travelAndStayResponse.value = Generic.Loading
            try {
                val response = repo.getTravelAndStayData(hotelStayId, category)
                _travelAndStayResponse.value = response
                fetchedHotelStayId = hotelStayId
            } catch (e: Exception) {
                _travelAndStayResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }

    }

}