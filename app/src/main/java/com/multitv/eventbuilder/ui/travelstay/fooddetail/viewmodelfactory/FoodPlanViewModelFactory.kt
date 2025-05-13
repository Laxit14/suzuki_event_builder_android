package com.multitv.eventbuilder.ui.travelstay.fooddetail.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.travelstay.dodonot.viewmodel.DoDoNotViewModel
import com.multitv.eventbuilder.ui.travelstay.fooddetail.viewmodel.FoodPlanViewModel

class FoodPlanViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodPlanViewModel::class.java)) {
            return FoodPlanViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}