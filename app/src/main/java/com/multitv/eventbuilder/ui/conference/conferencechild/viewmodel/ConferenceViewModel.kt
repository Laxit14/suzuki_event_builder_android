package com.multitv.eventbuilder.ui.conference.conferencechild.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.conference.model.ConferenceDataResponse
import kotlinx.coroutines.launch

class ConferenceViewModel(private val repo: Repo) : ViewModel() {

    private var _conferenceResponse = MutableLiveData<Generic<ConferenceDataResponse>>()
    val conferenceResponse: LiveData<Generic<ConferenceDataResponse>> get() = _conferenceResponse

    private var fetchedParentId: Long? = null

    fun getConferenceData(url:String){
        Log.d("ConferenceViewModel", "Calling API for Conference Data")

       /* if (fetchedParentId== parentId) return*/

        viewModelScope.launch {
            _conferenceResponse.value = Generic.Loading
            try {
                val response = repo.getConferenceData(url)
                _conferenceResponse.value = response

               /* if (response is Generic.Success) {
                    fetchedParentId = parentId // ✅ Set flag after successful fetch
                }*/

            } catch (e: Exception) {
                _conferenceResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}