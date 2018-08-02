package com.example.vmedvediev.ua21summerdancecamp.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmEvent(
        @PrimaryKey var id: String = "",
        var name: String = "",
        var date: String = "",
        var noteText: String = "",
        var noteDateChanged: String = "",
        var dateType: Int = 0
) : RealmObject(), ListItem {

    override fun getType() = dateType

    override fun getDateOfEvent() = date
}

@SuppressLint("ParcelCreator")
data class Event(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("date") var date: String = "",
        var noteText: String = "",
        var noteDateChanged: String = "") : ListItem {

    private val eventType: Int = 0

    override fun getDateOfEvent() = date

    override fun getType() = eventType
}

data class Date(
        var name: String = ""
) : ListItem {

    private val dateType: Int = 1

    override fun getDateOfEvent() = name

    override fun getType(): Int = dateType
}

interface ListItem {

    companion object {
        const val EVENT_TYPE = 0
        const val DATE_TYPE = 1
    }

    fun getType(): Int

    fun getDateOfEvent(): String
}