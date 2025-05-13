package com.multitv.eventbuilder.ui.interaction.digital_exhibition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.digitalexhibitionmodel.model.DigitalExhibitionResponse
import kotlinx.coroutines.launch

class DigitalExhibitionViewModel(private val repo: Repo) : ViewModel() {


    private val _digitalExhibitionLiveData = MutableLiveData<Generic<DigitalExhibitionResponse>>()
    val digitalExhibitionLiveData: LiveData<Generic<DigitalExhibitionResponse>> get() = _digitalExhibitionLiveData

    fun getDigitalExhibitionData(url : String,userId : Long){
        viewModelScope.launch {
            _digitalExhibitionLiveData.value = Generic.Loading
            try {
                val response = repo.getDigitalExhibitionData(url,userId)
                _digitalExhibitionLiveData.value = response
            } catch (e: Exception) {
                _digitalExhibitionLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}