package com.multitv.eventbuilder.ui.interaction.localization.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.localizationmodel.model.LocalizationResponse
import kotlinx.coroutines.launch

class LocalizationViewModel(private val repo: Repo) : ViewModel() {

    private val _localizationLivedata = MutableLiveData<Generic<LocalizationResponse>>()
    val localizationLivedata: LiveData<Generic<LocalizationResponse>> get() = _localizationLivedata

    fun getLocalData(url : String) {
        viewModelScope.launch {
            _localizationLivedata.value = Generic.Loading
            try {
                val response = repo.getLocalizationData(url)
                _localizationLivedata.value = response
            } catch (e: Exception) {
                _localizationLivedata.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
}