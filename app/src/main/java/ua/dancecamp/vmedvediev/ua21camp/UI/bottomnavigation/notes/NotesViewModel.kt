package ua.dancecamp.vmedvediev.ua21camp.UI.bottomnavigation.notes

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ua.dancecamp.vmedvediev.ua21camp.model.Event
import ua.dancecamp.vmedvediev.ua21camp.repository.Repository

class NotesViewModel(private val repository: Repository) : ViewModel() {

    val events: MutableLiveData<ArrayList<Event>> = MutableLiveData()
    val event: MutableLiveData<Event> = MutableLiveData()

    fun getEvents() {
        repository.getNotes { notesList: ArrayList<Event> -> if (notesList.isNotEmpty()) events.value = notesList}
    }

    fun getEvent(eventId: String) {
        repository.getEvent(eventId) { loadedEvent: Event? -> if (loadedEvent != null) event.value = loadedEvent }
    }

    fun deleteNoteFromDatabase(event: Event) {
        repository.deleteNoteFromDatabase(event)
    }

    fun saveNoteToDatabase(event: Event) {
        repository.saveNoteToDatabase(event)
    }

    inner class NotesViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NotesViewModel(repository) as T
        }
    }
}