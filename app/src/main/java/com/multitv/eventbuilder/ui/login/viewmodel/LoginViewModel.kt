package com.multitv.eventbuilder.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.model.loginimage.LoginImage
import com.multitv.eventbuilder.model.loginmodel.model.LoginResponse
import com.multitv.eventbuilder.sealed.Generic
import kotlinx.coroutines.launch
import java.io.File

class LoginViewModel(private val repo: Repo) : ViewModel() {

    private val _loginResponse = MutableLiveData<Generic<LoginResponse>>()
    val loginResponse: LiveData<Generic<LoginResponse>> get() = _loginResponse

    fun loginUser(mobile: String, token: String) {
        viewModelScope.launch {
            _loginResponse.value = Generic.Loading
            try {
                val response = repo.getLogin(mobile, token)
                _loginResponse.value = response
            } catch (e: Exception) {
                _loginResponse.value = Generic.Error(e.message ?: "Something went wrong")
            }
        }
    }


    private val _uploadResult = MutableLiveData<Generic<LoginImage>>()
    val uploadResult: LiveData<Generic<LoginImage>> get() = _uploadResult

    fun uploadImage(url: String, id: String, imageFile: File) {
        viewModelScope.launch {
            val result = repo.uploadImage(url, id, imageFile)
            _uploadResult.postValue(result)

        }

    }
}
