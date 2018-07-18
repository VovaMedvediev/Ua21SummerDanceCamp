package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.LiveData
import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.example.vmedvediev.ua21summerdancecamp.MyApplication
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.inflate
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import kotlinx.android.synthetic.main.day_item.view.*
import kotlinx.android.synthetic.main.event_item.view.*

class EventsAdapter(private val context: Context, private val eventsList: LiveData<ArrayList<Event>>) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val namesOfDaysArray = MyApplication.instance.resources.getStringArray(R.array.namesOfDaysArray)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> EventViewHolder(parent)
            2 -> DayViewHolder(parent)
            else -> EventViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                Log.e("ON BIND VIEW HOLDER::::", position.toString())
                val event = eventsList.value?.get(position)
                val eventHolder = holder as EventViewHolder
                eventHolder.id.text = event?.id
                eventHolder.name.text = event?.name
                eventHolder.card.setOnClickListener {
                    val intent = Intent(context, EventsActivity::class.java)
                    context.startActivity(intent)
                }
            }
            2 -> {
                Log.e("ITEM VIEW TYPE::::", position.toString())
                val nameOfTheDay = namesOfDaysArray[position]
                val dayHolder = holder as DayViewHolder
                dayHolder.day.text = nameOfTheDay
            }
        }
    }

    override fun getItemCount() : Int {
        return eventsList.value?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (eventsList.value?.get(position) != null) {
            Log.e("ITEM VIEW TYPE::::", position.toString())
            2
        } else {
            Log.e("ITEM VIEW TYPE::::", position.toString())
            0
        }
    }



    inner class EventViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(parent?.inflate(R.layout.event_item)) {

        val card: CardView = itemView.eventCardView
        val id: TextView = itemView.idTextView
        val name: TextView = itemView.nameTextView
    }

    inner class DayViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(parent?.inflate(R.layout.day_item)) {

        val day: TextView = itemView.dayTextView
    }
}