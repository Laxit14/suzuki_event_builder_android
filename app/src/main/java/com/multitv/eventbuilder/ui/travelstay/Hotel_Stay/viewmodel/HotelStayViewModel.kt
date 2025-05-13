package com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.hotelstaymodel.model.HotelStayResponse
import kotlinx.coroutines.launch

class HotelStayViewModel(private val repo: Repo) : ViewModel() {

    private val _hotelStayResponse = MutableLiveData<Generic<HotelStayResponse>>()
    val hotelStayResponse : LiveData<Generic<HotelStayResponse>> get() = _hotelStayResponse

    fun getHotelStayData(url : String) {
        viewModelScope.launch {
            _hotelStayResponse.value = Generic.Loading
            try {
                val response = repo.getHotelStayData(url)
                _hotelStayResponse.value = response
            } catch (e: Exception) {
                _hotelStayResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}