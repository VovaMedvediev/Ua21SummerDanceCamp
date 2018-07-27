package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.inflate
import com.example.vmedvediev.ua21summerdancecamp.model.Date
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import kotlinx.android.synthetic.main.day_item.view.*
import kotlinx.android.synthetic.main.event_item.view.*
import timber.log.Timber

class EventsAdapter(private val context: Context, private val eventsList: ArrayList<ListItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> EventViewHolder(parent)
            1 -> DayViewHolder(parent)
            else -> EventViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView
        when (holder.itemViewType) {
            0 -> {
                val event = eventsList[position] as Event
                val eventHolder = holder as EventViewHolder
                eventHolder.name.text = event.name
                eventHolder.card.setOnClickListener {

                }
            }
            1 -> {
                val date = eventsList[position] as Date
                val dayHolder = holder as DayViewHolder
                dayHolder.day.text = date.name
            }
        }
    }

    override fun getItemCount() = eventsList.size

    override fun getItemViewType(position: Int) = eventsList[position].getType()

    fun clearAndAddAll(updatedList: ArrayList<ListItem>) {
        eventsList.apply {
            clear()
            addAll(updatedList)
        }
    }

    fun addAll(updatedList: ArrayList<ListItem>) {
        if (!eventsList.containsAll(updatedList)) {
            eventsList.addAll(updatedList)
        }
    }

    fun getItem(position: Int) : ListItem {
        return try {
            eventsList[position]
        } catch (e: IndexOutOfBoundsException) {
            Event()
        }
    }

    inner class EventViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(parent?.inflate(R.layout.event_item)) {

        val card: CardView = itemView.eventCardView
        val name: TextView = itemView.nameTextView

    }

    inner class DayViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(parent?.inflate(R.layout.day_item)) {

        val day: TextView = itemView.dayTextView

    }
}