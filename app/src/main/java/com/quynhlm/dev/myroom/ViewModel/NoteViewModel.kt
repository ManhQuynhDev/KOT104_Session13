package com.quynhlm.dev.myroom.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quynhlm.dev.myroom.Models.Note
import com.quynhlm.dev.myroom.responsitory.NoteRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel (application: Application) : ViewModel() {

    private var respository : NoteRespository = NoteRespository(application)

    fun insertNote(note: Note) = viewModelScope.launch {
        respository.insertNote(note)
    }

    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        respository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        respository.deleteNote(note)
    }

    fun getAllNote() : LiveData<List<Note>> = respository.getAllNote()

    class NoteViewModelFactory(private var application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NoteViewModel::class.java)){
                return NoteViewModel(application) as T
            }
            throw  IllegalArgumentException("Unable construct viewModel")
        }
    }
}