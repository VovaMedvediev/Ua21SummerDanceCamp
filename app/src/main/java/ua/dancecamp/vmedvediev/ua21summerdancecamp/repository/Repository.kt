package ua.dancecamp.vmedvediev.ua21summerdancecamp.repository

import io.realm.RealmResults
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmCredentialsMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.entity.RealmEvent
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

class Repository(private val eventsMapper: RealmEventMapper,
                 private val settingsMapper: RealmSettingsMapper,
                 private val credentialsMapper: RealmCredentialsMapper) {

    companion object {
        private const val ERROR_CODE = "400"
    }

    fun getNotes(onEventsLoaded: (ArrayList<Event>) -> Unit) {
        val realmEventsList = DatabaseHelper.getEvents()
        onEventsLoaded(if (realmEventsList.isNotEmpty()) prepareNotesFromDatabase(realmEventsList) else ArrayList())
    }

    fun getEvent(eventId: String, onEventLoaded: (Event) -> Unit) {
        val realmEvent = DatabaseHelper.getEventById(eventId)
        if (realmEvent != null) {
            onEventLoaded(eventsMapper.from(realmEvent))
        } else {
            onEventLoaded(Event())
        }
    }

    fun getApplicationSettings(onApplicationSettingsLoaded: (ApplicationSettings) -> Unit) {
        val realmSettings = DatabaseHelper.getApplicationSettings()
        return if (realmSettings != null) {
            onApplicationSettingsLoaded(settingsMapper.from(realmSettings))
        } else {
            onApplicationSettingsLoaded(ApplicationSettings())
        }
    }

    fun getCredentials(onCredentialsLoaded: (Credentials) -> Unit) {
        val realmCredentials = DatabaseHelper.getCredentials()
        return if (realmCredentials != null) {
            onCredentialsLoaded(credentialsMapper.from(realmCredentials))
        } else {
            onCredentialsLoaded(Credentials())
        }
    }
        fun getWeatherResponse(onWeatherResponseLoaded: (WeatherResponse) -> Unit) {
        val weatherApi = NetworkManager.initRetrofit()
        val weatherResponse = weatherApi.getCurrentWeather()
        weatherResponse.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                onWeatherResponseLoaded(WeatherResponse("", ERROR_CODE))
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val body = response.body()
                if (body != null) {
                    onWeatherResponseLoaded(body)
                }
            }

        })
    }

    fun deleteNoteFromDatabase(event: Event) {
        DatabaseHelper.deleteNote(eventsMapper.to(event))
    }

    fun saveNoteToDatabase(event: Event) {
        DatabaseHelper.saveNote(eventsMapper.to(event))
    }

    private fun prepareNotesFromDatabase(realmEventsList: RealmResults<RealmEvent>): ArrayList<Event> {
        val eventsList = ArrayList<Event>()
        val filteredList = realmEventsList.filter { it.noteText.isNotEmpty() }
        filteredList.mapTo(eventsList) { it -> eventsMapper.from(it) }
        return eventsList
    }

    fun getEventsList(date: String, onDataLoaded: (LinkedHashSet<ListItem>) -> Unit) {
        val realmEventsList = DatabaseHelper.getEventsByDate(date)
        onDataLoaded(if (EventsCache.eventsList.isNotEmpty()) prepareEventsFromDatabase(realmEventsList) else if (realmEventsList.isNotEmpty()) prepareEventsFromDatabase(realmEventsList) else LinkedHashSet())
    }

    fun getAllEvents() = DatabaseHelper.getEvents()

    fun setupLocalStorage() {
        EventsCache.eventsList.clear()
        val realmEventsList = DatabaseHelper.getEvents()
        realmEventsList.mapTo(EventsCache.eventsList) {it -> eventsMapper.from(it) }
    }

    fun saveEvents(eventsList: ArrayList<Event>) {
        val realmEventsList = ArrayList<RealmEvent>()
        eventsList.forEach {
            realmEventsList.add(eventsMapper.to(it))
        }
        DatabaseHelper.saveEvents(realmEventsList)
    }

    fun saveApplicationSettings(applicationSettings: ApplicationSettings) {
        val realmSettings = settingsMapper.to(applicationSettings)
        DatabaseHelper.saveApplicationSettings(realmSettings)
    }

    fun saveCredentials(credentials: Credentials) {
        val realmCredentials = credentialsMapper.to(credentials)
        DatabaseHelper.saveCredentials(realmCredentials)
    }

    private fun getEventsFromLocalStorage(dateOfDay: String): LinkedHashSet<ListItem> {
        val eventsList = LinkedHashSet<ListItem>()
        EventsCache.getEventsByDate(dateOfDay).forEach {
            val date = Date(it.getDateOfEvent())
            eventsList.add(date)
            eventsList.add(it)
        }
        return eventsList
    }

    private fun prepareEventsFromDatabase(realmEventsList: RealmResults<RealmEvent>): LinkedHashSet<ListItem> {
        val eventsList = LinkedHashSet<ListItem>()
        realmEventsList.forEach {
            val date = Date(eventsMapper.from(it).eventDate)
            eventsList.apply {
                add(date)
                add(eventsMapper.from(it))
            }
        }
        return eventsList
    }
}