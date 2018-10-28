package ua.dancecamp.vmedvediev.ua21summerdancecamp.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApplicationSettings(
        var id: String = "1",
        var interfaceLanguage: String = "",
        var localeLanguage: String = ""
)

@SuppressLint("ParcelCreator")
data class Event(
        @SerializedName("id") var eventId: String = "",
        @SerializedName("name") var eventName: String = "",
        @SerializedName("date") var eventDate: String = "",
        var eventNoteText: String = "",
        var eventNoteDate: String = "",
        val eventType: Int = 0,
        @SerializedName("time") var eventTime: String = "",
        var canHaveNote: Boolean = false,
        @SerializedName("image") var eventImage: String = "") : ListItem {

    override fun getDateOfEvent() = eventDate

    override fun getType() = eventType
}

data class Date(
        var name: String = "", val dateType: Int = 1
) : ListItem {
    override fun getType() = dateType

    override fun getDateOfEvent() = name
}

data class WeatherResponse(
        @Expose var message: String = "",
        @Expose var cod: String = "",
        @Expose var count: Int = 0,
        @Expose var list: ArrayList<List> = ArrayList()
)

data class List(
        @Expose var id: Int,
        @Expose var name: String,
        @Expose var coord: Coord,
        @Expose var main: Main,
        @Expose var dt: Int,
        @Expose var wind: Wind,
        @Expose var sys: Sys,
        @Expose var rain: Any,
        @Expose var snow: Any,
        @Expose var clouds: Clouds,
        @Expose var weather: ArrayList<Weather>
)

data class Coord(
        @Expose var lat: Double,
        @Expose var lon: Double
)

data class Main(
        @Expose var temp: Int,
        @Expose var pressure: Int,
        @Expose var humidity: Int,
        @SerializedName("temp_min") var minTemp: Int,
        @SerializedName("temp_max") var maxTemp: Int
)

data class Wind(
        @Expose var speed: Double,
        @Expose var deg: Int
)

data class Sys(
        @Expose var country: String
)

data class Clouds(
        @Expose var all: Int
)

data class Weather(
        @Expose var id: Int,
        @Expose var main: String,
        @Expose var description: String,
        @Expose var icon: String
)

interface ListItem {

    companion object {
        const val EVENT_TYPE = 0
        const val DATE_TYPE = 1
    }

    fun getType(): Int

    fun getDateOfEvent(): String
}