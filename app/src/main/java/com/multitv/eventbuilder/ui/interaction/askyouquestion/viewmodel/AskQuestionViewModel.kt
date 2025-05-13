package com.multitv.eventbuilder.ui.interaction.askyouquestion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.askmodel.model.AskQuestionResponse
import kotlinx.coroutines.launch

class AskQuestionViewModel(private val repo: Repo) : ViewModel() {


    private val _askQuestResponse = MutableLiveData<Generic<AskQuestionResponse>>()
    val askQuestResponse: LiveData<Generic<AskQuestionResponse>> get() = _askQuestResponse

    fun postQuestionFeed(url : String, userId : Long,question : String,eventId : String,audiId : String) {
        viewModelScope.launch {
            _askQuestResponse.value = Generic.Loading
            try {
                val response = repo.postQuestion(url,userId,question,eventId,audiId)
                _askQuestResponse.value = response
            } catch (e: Exception) {
                _askQuestResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}