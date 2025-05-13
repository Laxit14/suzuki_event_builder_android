package com.multitv.eventbuilder.ui.interaction.feedback_survey.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.interaction.feedback_survey.viewmodel.FeedbackViewModel
import com.multitv.eventbuilder.ui.travelstay.fooddetail.viewmodel.FoodPlanViewModel

class FeedbackViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
            return FeedbackViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}