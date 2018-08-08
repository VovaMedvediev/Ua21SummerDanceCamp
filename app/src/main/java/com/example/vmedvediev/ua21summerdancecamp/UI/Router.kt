package com.example.vmedvediev.ua21summerdancecamp.UI

import android.content.Context
import android.content.Intent
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes.NoteActivity
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes.NoteActivity.Companion.KEY_EVENT_ID_TO_NOTE_ACTIVITY

object Router {

    fun prepareMainActivityIntent(context: Context) = Intent(context, MainActivity::class.java)

    fun prepareNoteActivityIntent(context: Context, eventId: String) : Intent {
        val intent = Intent(context, NoteActivity::class.java)
        intent.putExtra(KEY_EVENT_ID_TO_NOTE_ACTIVITY, eventId)
        return intent
    }
}