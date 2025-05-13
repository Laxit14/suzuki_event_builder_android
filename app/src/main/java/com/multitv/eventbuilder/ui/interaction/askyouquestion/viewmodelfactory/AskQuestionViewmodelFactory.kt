package com.multitv.eventbuilder.ui.interaction.askyouquestion.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.interaction.askyouquestion.viewmodel.AskQuestionViewModel
import com.multitv.eventbuilder.ui.login.viewmodel.LoginViewModel

class AskQuestionViewmodelFactory(private val repo : Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(AskQuestionViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AskQuestionViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}