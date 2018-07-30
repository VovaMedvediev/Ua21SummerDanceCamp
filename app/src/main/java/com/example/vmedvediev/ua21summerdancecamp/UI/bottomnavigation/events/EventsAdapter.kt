package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.inflate
import com.example.vmedvediev.ua21summerdancecamp.model.Date
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.ListItem
import kotlinx.android.synthetic.main.day_item.view.*
import kotlinx.android.synthetic.main.event_item.view.*

class EventsAdapter(private val context: Context, private val eventsList: ArrayList<ListItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val EVENT_TYPE = 0
        const val DATE_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EVENT_TYPE -> EventViewHolder(parent)
            DATE_TYPE -> DayViewHolder(parent)
            else -> EventViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView
        when (holder.itemViewType) {
            EVENT_TYPE -> prepareEventCard(holder, position)
            DATE_TYPE -> prepareDateCard(holder, position)
        }
    }

    private fun prepareEventCard(holder: RecyclerView.ViewHolder, position: Int) {
        val event = eventsList[position] as Event
        val eventHolder = holder as EventViewHolder
        eventHolder.name.text = event.name
        eventHolder.card.setOnClickListener {

        }
    }

    private fun prepareDateCard(holder: RecyclerView.ViewHolder, position: Int) {
        val date = eventsList[position] as Date
        val dayHolder = holder as DayViewHolder
        dayHolder.day.text = date.name
    }

    override fun getItemCount() = eventsList.size

    override fun getItemViewType(position: Int) = eventsList[position].getType()

    fun clearAndAddAll(updateList: ArrayList<ListItem>) {
        eventsList.apply {
            clear()
            addAll(updateList)
        }
    }

    fun addAll(updateList: ArrayList<ListItem>) {
        if (!eventsList.containsAll(updateList)) {
            eventsList.addAll(updateList)
        }
    }

    fun getItem(position: Int) = eventsList[position]

    inner class EventViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(parent?.inflate(R.layout.event_item)) {

        val card: CardView = itemView.eventCardView
        val name: TextView = itemView.eventNameTextView

    }

    inner class DayViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(parent?.inflate(R.layout.day_item)) {

        val day: TextView = itemView.dayTextView

    }
}