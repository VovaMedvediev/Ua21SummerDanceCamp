package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import android.arch.lifecycle.Observer
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Event
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository
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
        ViewModelProviders.of(this, NotesViewModel(Repository(RealmEventMapper())).NotesViewModelFactory()).
                get(NotesViewModel::class.java)
    }
    private val event: Event? by lazy {
        notesViewModel.event.value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        setupEvent()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
            event?.let {
                val eventName = it.eventName
                toolbarTitleTextView.text = eventName
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note, menu)
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
                notesViewModel.event.value?.let {
                    event?.let { saveNoteToDatabase(Event(eventId, it.eventName,
                            it.eventDate, getNoteText(), prepareNoteDate(), it.eventType, it.eventTime,
                            it.canHaveNote, it.eventImage)) }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupEvent() {
        notesViewModel.apply {
            getEvent(eventId)
            event.observe(this@NoteActivity, Observer {
                if (it != null) {
                    noteEditText.setText(it.eventNoteText)
                }
            })
        }
    }

    private fun saveNoteToDatabase(event: Event) {
        notesViewModel.saveNoteToDatabase(event)
    }

    private fun getNoteText() = noteEditText?.text.toString()

    private fun prepareNoteDate() : String {
        val dateFormat = SimpleDateFormat("dd EEE HH:mm")
        val currentTime = Calendar.getInstance().time
        return dateFormat.format(currentTime)
    }
}