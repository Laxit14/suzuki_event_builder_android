package com.multitv.eventbuilder.ui.conference.dresscode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.dresscode.model.DressCodeResponse
import kotlinx.coroutines.launch

class DressViewModel(private val repo: Repo) : ViewModel() {

    private val _dressCodeResponse = MutableLiveData<Generic<DressCodeResponse>>()
    val dressCodeResponse: LiveData<Generic<DressCodeResponse>> get() = _dressCodeResponse

    fun getDressCode(url : String) {
        viewModelScope.launch {
            _dressCodeResponse.value = Generic.Loading
            try {
                val response = repo.getDressCodeData(url)
                _dressCodeResponse.value = response
            } catch (e: Exception) {
                _dressCodeResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}