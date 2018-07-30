package com.example.vmedvediev.ua21summerdancecamp.UI

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.vmedvediev.ua21summerdancecamp.MyApplication
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import java.io.IOException
import java.lang.ref.WeakReference
import java.nio.charset.Charset

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        when {
            LocalStorage.eventsList.isNotEmpty() -> {
                startActivity(Router.prepareMainActivityIntent(this))
                finish()
            }
            EventsRepository.getAllEvents().isNotEmpty() -> {
                setupLocalStorage()
                startActivity(Router.prepareMainActivityIntent(this))
                finish()
            }
            else -> JsonParserAsyncTask(WeakReference(this)).execute()
        }
    }

    private fun prepareRealmData(eventsList: ArrayList<Event>) {
        val eventsMapper = EventsMapper()
        val realmEventsList = ArrayList<RealmEvent>()
        eventsList.forEach {
            realmEventsList.add(eventsMapper.to(it))
        }
        setRealmData(realmEventsList)
        setupLocalStorage()
    }

    private fun setRealmData(realmEventsList: ArrayList<RealmEvent>) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            insert(realmEventsList)
            commitTransaction()
        }
    }

    private fun setupLocalStorage() {
        val eventsMapper = EventsMapper()
        val realmEventsList = EventsRepository.getAllEvents()
        realmEventsList.forEach {
            LocalStorage.eventsList.add(eventsMapper.from(it))
        }
    }

    companion object {
        class JsonParserAsyncTask(private val splashActivity: WeakReference<SplashActivity>) : AsyncTask<Void, Void, ArrayList<Event>>() {

            override fun doInBackground(vararg params: Void?): ArrayList<Event> {
                Thread.sleep(2000)
                 return try {
                    val inputStream = MyApplication.instance.applicationContext.assets.open(MyApplication.instance.getString(R.string.list_of_events_json))
                    val size = inputStream.available()
                    val buffer = ByteArray(size)
                    inputStream.read(buffer)
                    inputStream.close()
                    val json = String(buffer, Charset.defaultCharset())
                    Gson().fromJson(json, object : TypeToken<ArrayList<Event>>(){}.type)
                } catch (e: IOException) {
                    e.printStackTrace()
                    ArrayList()
                }
            }

            override fun onPostExecute(result: ArrayList<Event>?) {
                super.onPostExecute(result)
                splashActivity.get()?.apply {
                    result?.let { prepareRealmData(it) }
                    startActivity(Router.prepareMainActivityIntent(this))
                    finish()
                }
            }
        }
    }
}