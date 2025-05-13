package com.multitv.eventbuilder.ui.interaction.interactionchild.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.interactionmodel.model.InteractionResponse
import kotlinx.coroutines.launch

class InteractionViewModel(private val repo: Repo) : ViewModel() {

    private val _interactionLiveData = MutableLiveData<Generic<InteractionResponse>>()
    val interactionLiveData: LiveData<Generic<InteractionResponse>> get() = _interactionLiveData

    fun getInteractionData(url : String){
        viewModelScope.launch {
            _interactionLiveData.value = Generic.Loading
            try {
                val response = repo.getInteractionData(url)
                _interactionLiveData.value = response
            } catch (e: Exception) {
                _interactionLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
}