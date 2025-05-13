package com.multitv.eventbuilder.ui.travelstay.aboutanatalya.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.travelstay.aboutanatalya.viewmodel.AboutDohaViewModel
import com.multitv.eventbuilder.ui.travelstay.travelstayContent.viewmodel.TravelAndStayViewModel

class AboutDohaViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AboutDohaViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AboutDohaViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }

}