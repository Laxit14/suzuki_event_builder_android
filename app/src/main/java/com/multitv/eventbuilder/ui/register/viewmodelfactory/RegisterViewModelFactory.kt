package com.multitv.eventbuilder.ui.register.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.register.viewmodel.RegisterViewModel
import com.multitv.eventbuilder.ui.splash.viewmodel.SplashViewModel

class RegisterViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}