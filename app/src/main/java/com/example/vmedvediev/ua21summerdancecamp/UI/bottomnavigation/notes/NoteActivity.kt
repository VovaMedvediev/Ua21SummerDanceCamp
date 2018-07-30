package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    companion object {
        const val KEY_EVENT_TO_NOTE_ACTIVITY = "KEY_EVENT_TO_NOTE_ACTIVITY"
    }

    private val event: Event by lazy {
        var event = Event()
        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey(KEY_EVENT_TO_NOTE_ACTIVITY)) {
                event = extras.getParcelable(KEY_EVENT_TO_NOTE_ACTIVITY)
            }
        }
        return@lazy event
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = event.name
            subtitle = event.date
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
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}