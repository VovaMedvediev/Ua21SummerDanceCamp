package com.example.vmedvediev.ua21summerdancecamp.UI

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.vmedvediev.ua21summerdancecamp.MyApplication
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import timber.log.Timber
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
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            RealmController.getAllEvents().isNotEmpty() -> {
                setupLocalStorage()
                startActivity(Intent(this, MainActivity::class.java))
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
        val realmEventsList = RealmController.getAllEvents()
        if (realmEventsList.isNotEmpty()) {
            realmEventsList.forEach {
                LocalStorage.eventsList.add(eventsMapper.from(it))
            }
        }
    }

    companion object {
        class JsonParserAsyncTask(private val splashActivity: WeakReference<SplashActivity>) : AsyncTask<Void, Void, ArrayList<Event>>() {

            override fun doInBackground(vararg params: Void?): ArrayList<Event> {
                Thread.sleep(2000)
                lateinit var eventsList: ArrayList<Event>
                try {
                    val inputStream = MyApplication.instance.applicationContext.assets.open("ListOfEvents.json")
                    val size = inputStream.available()
                    val buffer = ByteArray(size)
                    inputStream.read(buffer)
                    inputStream.close()
                    val json = String(buffer, Charset.defaultCharset())
                    eventsList = Gson().fromJson(json, object : TypeToken<ArrayList<Event>>(){}.type)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return eventsList
            }

            override fun onPostExecute(result: ArrayList<Event>?) {
                super.onPostExecute(result)
                splashActivity.get()?.apply {
                    result?.let { prepareRealmData(it) }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}