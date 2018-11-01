package ua.dancecamp.vmedvediev.ua21summerdancecamp.model

import android.annotation.SuppressLint
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
        @SerializedName("message") var message: String = "",
        @SerializedName("cod") var cod: String = "",
        @SerializedName("count") var count: Int = 0,
        @SerializedName("list") var list: ArrayList<List> = ArrayList()
)

data class List(
        @SerializedName("id") var id: Int,
        @SerializedName("name") var name: String,
        @SerializedName("coord") var coord: Coord,
        @SerializedName("main") var main: Main,
        @SerializedName("dt") var dt: Int,
        @SerializedName("wind") var wind: Wind,
        @SerializedName("sys") var sys: Sys,
        @SerializedName("rain") var rain: Any,
        @SerializedName("snow") var snow: Any,
        @SerializedName("clouds") var clouds: Clouds,
        @SerializedName("weather") var weather: ArrayList<Weather>
)

data class Coord(
        @SerializedName("lat") var lat: Double,
        @SerializedName("lon") var lon: Double
)

data class Main(
        @SerializedName("temp") var temp: String,
        @SerializedName("pressure") var pressure: String,
        @SerializedName("humidity") var humidity: String,
        @SerializedName("temp_min") var minTemp: String,
        @SerializedName("temp_max") var maxTemp: String,
        @SerializedName("sea_level") var seaLevel: String,
        @SerializedName("grnd_level") var grndLevel: String
)

data class Wind(
        @SerializedName("speed") var speed: Double,
        @SerializedName("deg") var deg: String
)

data class Sys(
        @SerializedName("country") var country: String
)

data class Clouds(
        @SerializedName("all") var all: Int
)

data class Weather(
        @SerializedName("id") var id: Int,
        @SerializedName("main") var main: String,
        @SerializedName("description") var description: String,
        @SerializedName("icon") var icon: String
)

interface ListItem {

    companion object {
        const val EVENT_TYPE = 0
        const val DATE_TYPE = 1
    }

    fun getType(): Int

    fun getDateOfEvent(): String
}