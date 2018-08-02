package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.UI.Router
import com.example.vmedvediev.ua21summerdancecamp.inflate
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import kotlinx.android.synthetic.main.note_item.view.*

class NotesAdapter(private val context: Context, private val eventsList: ArrayList<Event>) :
        RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(parent)
    }

    override fun getItemCount() = eventsList.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        prepareNoteCard(holder, position)
    }

    private fun prepareNoteCard(holder: NotesViewHolder, position: Int) {
        val event = eventsList[position]
        holder.noteTitle.text = event.name
        holder.noteConstraintLayout.setOnClickListener {
            context.startActivity(Router.prepareNoteActivityIntentFromEvent(context, event.id))
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