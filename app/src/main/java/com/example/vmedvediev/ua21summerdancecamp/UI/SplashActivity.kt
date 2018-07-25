package com.example.vmedvediev.ua21summerdancecamp.UI

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.vmedvediev.ua21summerdancecamp.MyApplication
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.model.RealmEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.nio.charset.Charset

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (File(Realm.getDefaultConfiguration()?.path).exists()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
           JsonParserAsyncTask(WeakReference(this)).execute()
        }
    }

    private fun prepareRealmData(eventsList: ArrayList<Event>) {
        val eventsMapper = EventsMapper()
        val realmEventsList = ArrayList<RealmEvent>()
        eventsList.forEach {
            realmEventsList.add(eventsMapper.to(it))
        }
        setRealmData(realmEventsList)
    }

    private fun setRealmData(realmEventsList: ArrayList<RealmEvent>) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            insert(realmEventsList)
            commitTransaction()
        }
    }

    companion object {
        class JsonParserAsyncTask(private val splashActivity: WeakReference<SplashActivity>) : AsyncTask<Void, Void, ArrayList<Event>>() {

            override fun doInBackground(vararg params: Void?): ArrayList<Event> {
                Thread.sleep(2000)
                lateinit var eventsList: ArrayList<Event>
                try {
                    val inputStream = MyApplication.instance.applicationContext.assets.open("Test.json")
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