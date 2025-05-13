package com.multitv.eventbuilder.ui.interaction.interactionchild.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.viewmodel.PostNotesViewmodel
import com.multitv.eventbuilder.ui.interaction.interactionchild.viewmodel.InteractionViewModel

class InteractionViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InteractionViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return InteractionViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }

}