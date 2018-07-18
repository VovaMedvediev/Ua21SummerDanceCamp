package com.example.vmedvediev.ua21summerdancecamp.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmEvent(
        @PrimaryKey var id: String = "",
        var name: String = "",
        var date: String = ""
) : RealmObject()

data class Event(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("date") var date: String = "")


data class Data(
        @SerializedName("days") var days: Map<String, ArrayList<Event>>
)