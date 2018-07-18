package com.example.vmedvediev.ua21summerdancecamp.UI

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.vmedvediev.ua21summerdancecamp.MyApplication
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events.EventsActivity
import com.example.vmedvediev.ua21summerdancecamp.model.Data
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

    private fun prepareRealmData(data: Data) {
        val realmEventsList = ArrayList<RealmEvent>()
        data.days.forEach { (key, value) ->
            for (event in value) {
                realmEventsList.add(RealmEvent(event.id, event.name, key))
            }
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
        class JsonParserAsyncTask(private val splashActivity: WeakReference<SplashActivity>) : AsyncTask<Void, Void, Data>() {

            override fun doInBackground(vararg params: Void?): Data {
                Thread.sleep(2000)
                lateinit var data: Data
                try {
                    val inputStream = MyApplication.instance.applicationContext.assets.open("Events.json")
                    val size = inputStream.available()
                    val buffer = ByteArray(size)
                    inputStream.read(buffer)
                    inputStream.close()
                    val json = String(buffer, Charset.defaultCharset())
                    data = Gson().fromJson(json, object : TypeToken<Data>(){}.type)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return data
            }

            override fun onPostExecute(result: Data?) {
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