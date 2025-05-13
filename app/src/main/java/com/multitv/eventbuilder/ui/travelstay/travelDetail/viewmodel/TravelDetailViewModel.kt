package com.multitv.eventbuilder.ui.travelstay.travelDetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.traveldetailmodel.model.TravelDetailResponse
import kotlinx.coroutines.launch

class TravelDetailViewModel(private val repo: Repo) : ViewModel() {

    private val _travelDetailResponse = MutableLiveData<Generic<TravelDetailResponse>>()
    val travelDetailResponse: LiveData<Generic<TravelDetailResponse>> get() = _travelDetailResponse


     fun getTravelDetailResponse(url : String){
        viewModelScope.launch {
            _travelDetailResponse.value = Generic.Loading
            try {
                val response = repo.getTravelDetailResponse(url)
                _travelDetailResponse.value = response
            } catch (e: Exception) {
                _travelDetailResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}