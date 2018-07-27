package com.example.vmedvediev.ua21summerdancecamp.repository

import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmDateMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.ListItemDateMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import io.realm.RealmResults

class Repository(private val eventsMapper: EventsMapper, private val realmDateMapper: RealmDateMapper,
                 private val listItemDateMapper: ListItemDateMapper) {

    fun getEventsList(date: String, onDataLoaded: (ArrayList<ListItem>) -> Unit,
                      onDataNotLoaded: (ArrayList<ListItem>) -> Unit) {
        if (LocalStorage.eventsList.isNotEmpty()) {
            onDataLoaded(getDataFromLocalStorage(date))
        } else {
            val realmEventsList = RealmController.getEventsByDate(date)
            if (realmEventsList.isNotEmpty()) {
                onDataLoaded(prepareDataFromDatabase(realmEventsList))
            } else {
                onDataNotLoaded(ArrayList())
            }
        }
    }

    private fun getDataFromLocalStorage(dateOfDay: String): ArrayList<ListItem> {
        val eventsList = ArrayList<ListItem>()
        LocalStorage.getEventsByDate(dateOfDay).forEach {
            val date = listItemDateMapper.from(it)
            if (!eventsList.contains(date)) {
                eventsList.add(date)
            }
            eventsList.add(it)
        }
        return eventsList
    }

    private fun prepareDataFromDatabase(realmEventsList: RealmResults<RealmEvent>): ArrayList<ListItem> {
        val eventsList = ArrayList<ListItem>()
        realmEventsList.forEach {
            val date = realmDateMapper.from(it)
            if (!eventsList.contains(date)) {
                eventsList.add(date)
            }
            eventsList.add(eventsMapper.from(it))
        }
        return eventsList
    }
}