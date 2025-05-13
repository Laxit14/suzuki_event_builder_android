package com.multitv.eventbuilder.ui.travelstay.dodonot.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.dodonotmodel.model.DoDontResponse
import kotlinx.coroutines.launch

class DoDoNotViewModel(private val repo: Repo) : ViewModel() {

    private val _DoDoNotLiveData = MutableLiveData<Generic<DoDontResponse>>()
    val DoDoNotLiveData: LiveData<Generic<DoDontResponse>> get() = _DoDoNotLiveData

    fun getDoDoNotResponse(url : String) {
        viewModelScope.launch {
            _DoDoNotLiveData.value = Generic.Loading
            try {
                val response = repo.getDoDoNotResponse(url)
                _DoDoNotLiveData.value = response
            } catch (e: Exception) {
                _DoDoNotLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}