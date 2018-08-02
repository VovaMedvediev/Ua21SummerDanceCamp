package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.repository.NotesRepository
import kotlinx.android.synthetic.main.activity_note.*
import android.arch.lifecycle.Observer
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {

    companion object {
        const val KEY_EVENT_ID_TO_NOTE_ACTIVITY = "KEY_EVENT_ID_TO_NOTE_ACTIVITY"
    }

    private val eventId: String by lazy {
        var eventId = ""
        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey(KEY_EVENT_ID_TO_NOTE_ACTIVITY)) {
                eventId = extras.getString(KEY_EVENT_ID_TO_NOTE_ACTIVITY)
            }
        }
        return@lazy eventId
    }
    private val notesViewModel by lazy {
        ViewModelProviders.of(this, NotesViewModelFactory(NotesRepository(EventsMapper()))).get(NotesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        setupEvent()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            val event = notesViewModel.getEventValue()
            if (event.noteDateChanged.isNotEmpty()) {
                title = event.name
                subtitle = event.noteDateChanged
            } else {
                title = event.name
                subtitle = event.date
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val confirmNoteMenuItem = menu.findItem(R.id.confirmNoteMenuItem)
        confirmNoteMenuItem.setIcon(R.drawable.ic_done)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.confirmNoteMenuItem -> {
                saveNoteToDatabase(RealmEvent(eventId, notesViewModel.getEventValue().name,
                        notesViewModel.getEventValue().date, getNoteText(), prepareNoteDate()))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupEvent() {
        notesViewModel.apply {
            getEventFromRepository(eventId)
            getEvent().observe(this@NoteActivity, Observer {
                if (it != null) {
                    noteEditText.setText(it.noteText)
                }
            })
        }
    }

    private fun saveNoteToDatabase(realmEvent: RealmEvent) {
        notesViewModel.saveNoteToDatabase(realmEvent)
    }

    private fun getNoteText() = noteEditText?.text.toString()

    private fun prepareNoteDate() : String {
        val dateFormat = SimpleDateFormat("dd EEE HH:mm")
        val currentTime = Calendar.getInstance().time
        return dateFormat.format(currentTime)
    }
}