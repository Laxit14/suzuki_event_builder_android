package com.multitv.eventbuilder.ui.otp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.otpmodel.model.OtpResponse
import kotlinx.coroutines.launch

class OtpViewModel(val repo: Repo): ViewModel() {

    private val _otpResponse = MutableLiveData<Generic<OtpResponse>>()
    val otpResponse: LiveData<Generic<OtpResponse>> get() = _otpResponse

    fun verifyOty(mobile: String,otp : String) {
        viewModelScope.launch {
            _otpResponse.value = Generic.Loading
            try {
                val response = repo.verifyOtp(mobile,otp)
                _otpResponse.value = response
            } catch (e: Exception) {
                _otpResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }

}