package com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.viewmodel.HotelStayViewModel
import com.multitv.eventbuilder.ui.travelstay.aboutanatalya.viewmodel.AboutDohaViewModel

class HotelStayViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HotelStayViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return HotelStayViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }

}