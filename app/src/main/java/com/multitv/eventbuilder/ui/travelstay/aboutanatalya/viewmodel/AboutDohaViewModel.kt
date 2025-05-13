package com.multitv.eventbuilder.ui.travelstay.aboutanatalya.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.aboutdoha.model.AboutDohaResponse
import kotlinx.coroutines.launch

class AboutDohaViewModel(private val repo: Repo) : ViewModel() {

    private var _aboutResponse = MutableLiveData<Generic<AboutDohaResponse>>()
    val aboutResponse: LiveData<Generic<AboutDohaResponse>> get() = _aboutResponse


    fun getAboutDohaData(url: String) {
        viewModelScope.launch {
            _aboutResponse.value = Generic.Loading
            try {
                val response = repo.getAboutDohaData(url)
                _aboutResponse.value = response

                /*if (response is Generic.Success) {
                    matchBannerId = bannerId // ✅ Set flag after successful fetch
                }*/

            } catch (e: Exception) {
                _aboutResponse.value = Generic.Error(e.message ?: "Something went wrong")

            }
        }

    }

}