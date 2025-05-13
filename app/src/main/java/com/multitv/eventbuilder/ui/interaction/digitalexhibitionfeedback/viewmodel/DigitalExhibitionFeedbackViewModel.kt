package com.multitv.eventbuilder.ui.interaction.digitalexhibitionfeedback.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.digitalexhibitionfeedbackmodel.model.DigitalExhibitionFeedbackResponse
import kotlinx.coroutines.launch

class DigitalExhibitionFeedbackViewModel(private val repo: Repo) : ViewModel() {


    private val _digitalExhibitionfeedbackLiveData = MutableLiveData<Generic<DigitalExhibitionFeedbackResponse>>()
    val digitalExhibitionfeedbackLiveData: LiveData<Generic<DigitalExhibitionFeedbackResponse>> get() = _digitalExhibitionfeedbackLiveData

    fun getDigitalExhibitionFeedBackData(url : String,userId : Long,overallFeedback : String,content_feedback : String,
                                         organization_reason : String,organization_benefit : String,
                                         expectations : String,study_area : String,digilib_comment : String) {
        viewModelScope.launch {
            _digitalExhibitionfeedbackLiveData.value = Generic.Loading
            try {
                val response = repo.getDigitalExhibitionFeedbackResponse(url,userId,overallFeedback,content_feedback,organization_benefit
                    ,organization_reason,expectations,study_area,digilib_comment)
                _digitalExhibitionfeedbackLiveData.value = response
            } catch (e: Exception) {
                _digitalExhibitionfeedbackLiveData.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }


}