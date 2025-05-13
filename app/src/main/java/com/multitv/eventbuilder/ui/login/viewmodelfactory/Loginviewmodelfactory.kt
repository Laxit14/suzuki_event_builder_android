package com.multitv.eventbuilder.ui.login.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.login.viewmodel.LoginViewModel
import com.multitv.eventbuilder.ui.splash.viewmodel.SplashViewModel

class LoginViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(repo) as T
            }
            throw IllegalArgumentException("unknown viewmodel class")
    }
}