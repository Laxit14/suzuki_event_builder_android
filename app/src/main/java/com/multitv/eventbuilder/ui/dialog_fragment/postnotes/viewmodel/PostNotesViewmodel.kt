package com.multitv.eventbuilder.ui.dialog_fragment.postnotes.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.model.PostNotesResponse
import kotlinx.coroutines.launch

class PostNotesViewmodel(private val repo: Repo) : ViewModel() {

    private val _postNotesLiveData = MutableLiveData<Generic<PostNotesResponse>>()
    val postNotesLiveData: LiveData<Generic<PostNotesResponse>> get() = _postNotesLiveData

    fun getPostNotesResponse(url : String,speakerId:Int,userId : Long,content : String,tittle : String){
        viewModelScope.launch {
            _postNotesLiveData.value = Generic.Loading
            try {
                val response = repo.postNotes(url,speakerId,userId,content,tittle)
                _postNotesLiveData.value = response
            } catch (e: Exception) {
                Log.e("PostNotesViewmodel", "Error posting notes", e)
                _postNotesLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}