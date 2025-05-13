package com.multitv.eventbuilder.ui.conference.msilContact.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.msilcontact.model.MsilContactResponse
import kotlinx.coroutines.launch

class MsilContactViewModel(private val repo: Repo) : ViewModel() {


    private val _msilcontact = MutableLiveData<Generic<MsilContactResponse>>()
    val msilcontact: LiveData<Generic<MsilContactResponse>> get() = _msilcontact

    fun getMsilContactData(url : String) {
        viewModelScope.launch {
            _msilcontact.value = Generic.Loading
            try {
                val response = repo.getMsilContactData(url)
                _msilcontact.value = response
            } catch (e: Exception) {
                _msilcontact.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}