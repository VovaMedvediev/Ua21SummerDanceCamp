package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Event
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class NotesViewModel(private val repository: Repository) : ViewModel() {

    val events: MutableLiveData<ArrayList<Event>> = MutableLiveData()
    val event: MutableLiveData<Event> = MutableLiveData()

    fun getEvents() {
        repository.getNotes { notesList: ArrayList<Event> -> onEventsLoaded(notesList)}
    }

    fun getEvent(eventId: String) {
        repository.getEvent(eventId) { event: Event -> onEventLoaded(event)}
    }

    fun deleteNoteFromDatabase(event: Event) {
        repository.deleteNoteFromDatabase(event)
    }

    fun saveNoteToDatabase(event: Event) {
        repository.saveNoteToDatabase(event)
    }

    private fun onEventsLoaded(notesList: ArrayList<Event>) {
        if (notesList.isNotEmpty()) {
            events.value = notesList
        }
    }

    private fun onEventLoaded(loadedEvent: Event?) {
        if (loadedEvent != null) {
            event.value = loadedEvent
        }
    }

    inner class NotesViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NotesViewModel(repository) as T
        }
    }
}