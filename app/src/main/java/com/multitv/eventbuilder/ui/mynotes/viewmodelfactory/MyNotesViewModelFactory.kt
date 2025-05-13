package com.multitv.eventbuilder.ui.mynotes.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.ui.interaction.interactionchild.viewmodel.InteractionViewModel
import com.multitv.eventbuilder.ui.mynotes.viewModel.MyNotesViewModel

class MyNotesViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyNotesViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MyNotesViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}