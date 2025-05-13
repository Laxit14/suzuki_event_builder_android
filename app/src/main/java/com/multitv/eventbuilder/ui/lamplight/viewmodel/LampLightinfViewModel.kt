package com.multitv.eventbuilder.ui.lamplight.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.model.lamplightingmodel.PartsResponse
import com.multitv.eventbuilder.model.loginmodel.model.LoginResponse
import com.multitv.eventbuilder.sealed.Generic
import kotlinx.coroutines.launch

class LampLightinfViewModel(private val repo: Repo) : ViewModel() {


    private val _lampLiveData = MutableLiveData<Generic<PartsResponse>>()
    val lampLiveData: LiveData<Generic<PartsResponse>> get() = _lampLiveData

    fun getLampLighting(url: String) {
        viewModelScope.launch {
            _lampLiveData.value = Generic.Loading
            try {
                val response = repo.getLampLightingResponse(url)
                _lampLiveData.value = response
            } catch (e: Exception) {
                _lampLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
}