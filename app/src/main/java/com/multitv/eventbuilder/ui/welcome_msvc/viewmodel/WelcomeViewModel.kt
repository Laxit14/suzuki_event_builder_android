package com.multitv.eventbuilder.ui.welcome_msvc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.welcomemsvcmodel.model.WelcomeMessageResponse
import kotlinx.coroutines.launch

class WelcomeViewModel(private val repo: Repo) : ViewModel() {


    private val _welcomeResponse = MutableLiveData<Generic<WelcomeMessageResponse>>()
    val welcomeResponse: LiveData<Generic<WelcomeMessageResponse>> get() = _welcomeResponse

  private var  matchBannerId :Int? = null

    fun getWelcomeResponse(url : String){
      /*  if (matchBannerId == bannerId) return*/


        viewModelScope.launch {
            _welcomeResponse.value = Generic.Loading
            try {
            val response  = repo.getWelcomeData(url)

                _welcomeResponse.value = response

                /*if (response is Generic.Success) {
                    matchBannerId = bannerId // ✅ Set flag after successful fetch
                }*/


            }
            catch (e : Exception){
                _welcomeResponse.value = Generic.Error(e.message ?: "Something went wrong")

            }
        }
    }

}