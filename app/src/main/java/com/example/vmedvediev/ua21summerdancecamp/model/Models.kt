package com.example.vmedvediev.ua21summerdancecamp.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmEvent(
        @PrimaryKey var id: String = "",
        var name: String = "",
        var date: String = "",
        var eventTime: String = "",
        var noteText: String = "",
        var noteDate: String = "",
        var dateType: Int = 0,
        var canHaveNote: Boolean = false
) : RealmObject()

@SuppressLint("ParcelCreator")
data class Event(
        @SerializedName("id") var eventId: String = "",
        @SerializedName("name") var eventName: String = "",
        @SerializedName("date") var eventDate: String = "",
        @SerializedName("time") var eventTime: String = "",
        var eventNoteText: String = "", var eventNoteDate: String = "", val eventType: Int = 0,
        var canHaveNote: Boolean = false) : ListItem {

    override fun getDateOfEvent() = eventDate

    override fun getType() = eventType
}

data class Date(
        var name: String = "", val dateType: Int = 1
) : ListItem {
    override fun getType() = dateType

    override fun getDateOfEvent() = name

}

interface ListItem {

    companion object {
        const val EVENT_TYPE = 0
        const val DATE_TYPE = 1
    }

    fun getType(): Int

    fun getDateOfEvent(): String
}