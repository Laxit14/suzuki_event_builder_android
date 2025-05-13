package com.multitv.eventbuilder.ui.interaction.feedback_survey.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.feedbackmodel.model.FeedbackResponse
import kotlinx.coroutines.launch

class FeedbackViewModel(private val repo: Repo) : ViewModel() {

    private val _feedbackLiveData = MutableLiveData<Generic<FeedbackResponse>>()
    val feedbackLiveData: LiveData<Generic<FeedbackResponse>> get() = _feedbackLiveData

    fun getFeedBackData(url : String,hotel_star : Float,hotel_comment : String,entertnmt_str : Float,
                          entertnmt_cmnt : String,
                          engagemnt_str : Float,engagmnt_cmnt : String,food_str : Float, food_cmnt : String,
                          overral_str : Float,overall_cmt : String,userId : String) {
        viewModelScope.launch {
            _feedbackLiveData.value = Generic.Loading
            try {
                val response = repo.getFeedbackResponse(url,hotel_star,hotel_comment,entertnmt_str,entertnmt_cmnt,
                    engagemnt_str,engagmnt_cmnt,food_str,food_cmnt,overral_str,overall_cmt,userId)
                _feedbackLiveData.value = response
            } catch (e: Exception) {
                _feedbackLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }


}