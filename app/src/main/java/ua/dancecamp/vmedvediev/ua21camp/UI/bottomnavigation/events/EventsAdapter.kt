package ua.dancecamp.vmedvediev.ua21camp.UI.bottomnavigation.events

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.day_item.view.*
import kotlinx.android.synthetic.main.event_item.view.*
import ua.dancecamp.vmedvediev.ua21camp.R
import ua.dancecamp.vmedvediev.ua21camp.services.inflate
import ua.dancecamp.vmedvediev.ua21camp.model.Date
import ua.dancecamp.vmedvediev.ua21camp.model.Event
import ua.dancecamp.vmedvediev.ua21camp.model.ListItem
import ua.dancecamp.vmedvediev.ua21camp.model.ListItem.Companion.DATE_TYPE
import ua.dancecamp.vmedvediev.ua21camp.model.ListItem.Companion.EVENT_TYPE

class EventsAdapter(private val context: Context, private val eventsList: MutableList<ListItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onEventClickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EVENT_TYPE -> EventViewHolder(parent)
            DATE_TYPE -> DayViewHolder(parent)
            else -> EventViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            EVENT_TYPE -> prepareEventCard(holder, position)
            DATE_TYPE -> prepareDateCard(holder, position)
        }
    }

    private fun prepareEventCard(holder: RecyclerView.ViewHolder, position: Int) {
        val event = eventsList[position] as Event
        val eventHolder = holder as EventViewHolder
        eventHolder.apply {
            name.text = event.eventName
            time.text = event.eventTime
            val imageId = context.resources
                    .getIdentifier(event.eventImage, "drawable", context.packageName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                eventImage.clipToOutline = true
            }
            eventImage.setImageResource(imageId)
            if (event.canHaveNote) {
                editNoteIcon.apply {
                    visibility = View.VISIBLE
                    setOnClickListener { onEventClickListener(event.eventId) }
                }
            } else {
                editNoteIcon.visibility = View.GONE
            }
        }
    }

    private fun prepareDateCard(holder: RecyclerView.ViewHolder, position: Int) {
        val date = eventsList[position] as Date
        val dayHolder = holder as DayViewHolder
        dayHolder.day.text = date.name
    }

    override fun getItemCount() = eventsList.size

    override fun getItemViewType(position: Int) = eventsList[position].getType()

    fun clearAndAddAll(updateList: MutableList<ListItem>) {
        eventsList.apply {
            clear()
            addAll(updateList)
        }
    }

    fun addAll(updateList: MutableList<ListItem>) = eventsList.addAll(updateList)

    fun getItem(position: Int) = eventsList[ if (position != -1) position else 1]

    inner class EventViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.event_item)) {

        val name: TextView = itemView.eventNameTextView
        val time: TextView = itemView.eventTimeTextView
        val editNoteIcon = itemView.editNoteImageView
        val eventImage = itemView.eventImageVIew

    }

    inner class DayViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.day_item)) {

        val day: TextView = itemView.dayTextView

    }
}