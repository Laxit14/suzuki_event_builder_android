package com.multitv.eventbuilder.ui.dialog_fragment.postnotes.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.viewmodel.PostNotesViewmodel
import com.multitv.eventbuilder.ui.interaction.currencyconvertor.viewmodel.CurrencyConvertorViewModel

class PostNotesViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PostNotesViewmodel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return PostNotesViewmodel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}