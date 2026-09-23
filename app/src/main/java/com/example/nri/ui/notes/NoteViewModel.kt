package com.example.nri.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nri.data.AppDatabase
import com.example.nri.data.Note
import com.example.nri.data.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = NoteRepository(AppDatabase.getInstance(app).noteDao())

    val notes: StateFlow<List<Note>> = repository.notes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(note: Note) {
        viewModelScope.launch {
            if (note.id == 0) repository.add(note) else repository.update(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch { repository.delete(note) }
    }
}
