package com.multitv.eventbuilder.ui.travelstay.fooddetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.fooddetailmodel.model.FoodScheduleResponse
import kotlinx.coroutines.launch

class FoodPlanViewModel(private val repo: Repo) : ViewModel() {

    private val _foodPlanLiveData = MutableLiveData<Generic<FoodScheduleResponse>>()
    val foodPlanLiveData: LiveData<Generic<FoodScheduleResponse>> get() = _foodPlanLiveData

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