package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.vmedvediev.ua21summerdancecamp.repository.NotesRepository

class NotesViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotesViewModel(repository) as T
    }
}