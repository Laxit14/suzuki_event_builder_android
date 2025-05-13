package com.multitv.eventbuilder.ui.otp.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.login.viewmodel.LoginViewModel
import com.multitv.eventbuilder.ui.otp.viewmodel.OtpViewModel

class OtpViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(OtpViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OtpViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}