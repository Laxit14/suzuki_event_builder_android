package com.multitv.eventbuilder.ui.conference.awards.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.award.model.AwardResponse
import kotlinx.coroutines.launch

class AwardsViewModel(private val repo: Repo) : ViewModel() {

    private val _awardsLiveData = MutableLiveData<Generic<AwardResponse>>()
    val awardsLiveData: LiveData<Generic<AwardResponse>> get() = _awardsLiveData

    fun getAwardImages(url : String,userId : Long) {
        viewModelScope.launch {
            _awardsLiveData.value = Generic.Loading
            try {
                val response = repo.getAwardsResponse(url,userId)
                _awardsLiveData.value = response
            } catch (e: Exception) {
                _awardsLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
}