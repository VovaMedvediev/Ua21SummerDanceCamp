package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.inflate
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import kotlinx.android.synthetic.main.note_item.view.*

class NotesAdapter(private val eventsList: ArrayList<Event>) :
        RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    var onNoteClickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(parent)
    }

    override fun getItemCount() = eventsList.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        prepareNoteCard(holder, position)
    }

    private fun prepareNoteCard(holder: NotesViewHolder, position: Int) {
        val event = eventsList[position]
        holder.apply {
            noteTitle.text = event.eventName
            noteConstraintLayout.setOnClickListener {
                onNoteClickListener(event.eventId)
            }
        }
    }

    fun clearAndAddAll(updateList: List<Event>) {
        eventsList.apply {
            clear()
            addAll(updateList)
        }
    }

    fun removeAt(position: Int) {
        eventsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Event {
        return eventsList[position]
    }

    inner class NotesViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(parent?.inflate(R.layout.note_item)) {

        val noteConstraintLayout = itemView.noteConstraintLayout
        val noteTitle: TextView = itemView.noteTitleTextView

    }
}