package com.multitv.eventbuilder.ui.welcome_msvc.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.home.viewmodel.HomeViewModel
import com.multitv.eventbuilder.ui.welcome_msvc.viewmodel.WelcomeViewModel

class WelcomeViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WelcomeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return WelcomeViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}