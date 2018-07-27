package com.example.vmedvediev.ua21summerdancecamp.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmEvent(
        @PrimaryKey var id: String = "",
        var name: String = "",
        var date: String = "",
        var dateType: Int = 0
) : RealmObject(), ListItem {

    override fun getType() = dateType

    override fun getDateOfEvent() = date

    constructor() : this("", "", "",0)

}

data class Event(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("date") var date: String = "",
        val eventType: Int = 0) : ListItem {

    override fun getDateOfEvent() = date

    override fun getType() = eventType
}

data class Date(
        var name: String = "",
        val dateType: Int = 1
) : ListItem {

    override fun getDateOfEvent() = name

    override fun getType(): Int = dateType
}

interface ListItem {

    fun getType(): Int

    fun getDateOfEvent(): String
}