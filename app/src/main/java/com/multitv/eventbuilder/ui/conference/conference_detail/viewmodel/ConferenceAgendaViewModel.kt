package com.multitv.eventbuilder.ui.conference.conference_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.confrence_detail.model.ConferenceAgendaDataResponse
import com.multitv.eventbuilder.model.confrence_detail.model.DetailAgendaResponse
import com.multitv.eventbuilder.model.confrence_detail.model.ShuttleBusAgendaResponse
import com.multitv.eventbuilder.model.confrence_detail.model.SpouseTourResponse
import com.multitv.eventbuilder.model.fooddetailmodel.model.FoodScheduleResponse
import kotlinx.coroutines.launch

class ConferenceAgendaViewModel(private val repo: Repo) : ViewModel() {

    private val _conferenceAgendaResponse = MutableLiveData<Generic<ConferenceAgendaDataResponse>>()
    val conferenceAgendaResponse: LiveData<Generic<ConferenceAgendaDataResponse>> get() = _conferenceAgendaResponse

    private val _shuttleBusDetail = MutableLiveData<Generic<ShuttleBusAgendaResponse>>()
    val shuttleBusDetail: LiveData<Generic<ShuttleBusAgendaResponse>> get() = _shuttleBusDetail

    private val _spouseTourResponse = MutableLiveData<Generic<SpouseTourResponse>>()
    val spouseTourResponse: LiveData<Generic<SpouseTourResponse>> get() = _spouseTourResponse

    private val _detailAgendaResponse = MutableLiveData<Generic<DetailAgendaResponse>>()
    val detailAgendaResponse: LiveData<Generic<DetailAgendaResponse>> get() = _detailAgendaResponse

    private val _foodPlanLiveData = MutableLiveData<Generic<FoodScheduleResponse>>()
    val foodPlanLiveData: LiveData<Generic<FoodScheduleResponse>> get() = _foodPlanLiveData

    fun getShuttleBusDetail(url : String) {
        viewModelScope.launch {
            _shuttleBusDetail.value = Generic.Loading
            try {
                val response = repo.getShuttleBusDetailData(url)
                _shuttleBusDetail.value = response
            } catch (e: Exception) {
                _shuttleBusDetail.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }


    fun getSpouseTourResponse(url : String) {
        viewModelScope.launch {
            _spouseTourResponse.value = Generic.Loading
            try {
                val response = repo.getSpouseTourData(url)
                _spouseTourResponse.value = response
            } catch (e: Exception) {
                _spouseTourResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }


    fun getDetailAgendaData(url : String) {
        viewModelScope.launch {
            _detailAgendaResponse.value = Generic.Loading
            try {
                val response = repo.getDetailAgendaData(url)
                _detailAgendaResponse.value = response
            } catch (e: Exception) {
                _detailAgendaResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getConferenceAgenda(url : String) {
        viewModelScope.launch {
            _conferenceAgendaResponse.value = Generic.Loading
            try {
                val response = repo.getConferenceAgenda(url)
                _conferenceAgendaResponse.value = response
            } catch (e: Exception) {
                _conferenceAgendaResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }



    fun getFoodPlanDetail(url : String) {
        viewModelScope.launch {
            _foodPlanLiveData.value = Generic.Loading
            try {
                val response = repo.getFoodPlan(url)
                _foodPlanLiveData.value = response
            } catch (e: Exception) {
                _foodPlanLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }


}