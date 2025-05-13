package com.multitv.eventbuilder.ui.interaction.currencyconvertor.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.interaction.currencyconvertor.viewmodel.CurrencyConvertorViewModel
import com.multitv.eventbuilder.ui.interaction.localization.viewmodel.LocalizationViewModel

class CurrencyConvertorViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CurrencyConvertorViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CurrencyConvertorViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}