package com.multitv.eventbuilder.ui.mynotes.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.mynotesmodel.model.MYNotesResponse
import kotlinx.coroutines.launch

class MyNotesViewModel(private val repo: Repo) : ViewModel() {

    private val _myNotesLiveData = MutableLiveData<Generic<MYNotesResponse>>()
    val myNotesLiveData: LiveData<Generic<MYNotesResponse>> get() = _myNotesLiveData

    fun getMyNotesData(url : String,userId:Long){
        viewModelScope.launch {
            _myNotesLiveData.value = Generic.Loading
            try {
                val response = repo.getMyAllNotsData(url,userId)
                _myNotesLiveData.value = response
            } catch (e: Exception) {
                Log.e("getMyNotesViewmodel", "Error posting notes", e)
                _myNotesLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }
}