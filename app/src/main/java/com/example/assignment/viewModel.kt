package com.example.assignment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class NotesViewModel : ViewModel() {

    private val _listOfNotes = MutableStateFlow<List<UiState>>(emptyList())
    val listOfNotes: StateFlow<List<UiState>> = _listOfNotes.asStateFlow()

    private val _isFormVisible = MutableStateFlow(false)
    val isFormVisible: StateFlow<Boolean> = _isFormVisible.asStateFlow()
    init {
        val initialList = listOf(
            UiState("Hi1", "Hello"),
            UiState("Hi2", "Hello"),
            UiState("Hi3", "Hello")
        )
        _listOfNotes.value = initialList
    }

    fun updateNotes(notesList: List<UiState>) {
        _listOfNotes.value = notesList
    }

    fun resetNotes() {
        _listOfNotes.value = emptyList()
    }

    fun addNote(note: UiState) {
        val currentList = _listOfNotes.value.toMutableList()
        currentList.add(note)
        _listOfNotes.value = currentList
    }

    fun removeNote(note: UiState) {
        val currentList = _listOfNotes.value.toMutableList()
        currentList.remove(note)
        _listOfNotes.value = currentList
    }
    fun showForm() {
        _isFormVisible.value = true
    }

    fun hideForm() {
        _isFormVisible.value = false
    }

    fun toggleFormVisibility() {
        _isFormVisible.value = !_isFormVisible.value
    }
}