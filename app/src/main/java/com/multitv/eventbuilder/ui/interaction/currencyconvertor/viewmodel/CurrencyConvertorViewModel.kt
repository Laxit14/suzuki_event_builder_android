package com.multitv.eventbuilder.ui.interaction.currencyconvertor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.currencymodel.model.CurrencyConversionResponse
import kotlinx.coroutines.launch

class CurrencyConvertorViewModel(private val repo: Repo) : ViewModel() {

    private val _currencyLivedata = MutableLiveData<Generic<CurrencyConversionResponse>>()
    val currencyLivedata: LiveData<Generic<CurrencyConversionResponse>> get() = _currencyLivedata

    fun getCurrencyData(url : String) {
        viewModelScope.launch {
            _currencyLivedata.value = Generic.Loading
            try {
                val response = repo.getCurrencyConvertor(url)
                _currencyLivedata.value = response
            } catch (e: Exception) {
                _currencyLivedata.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
}