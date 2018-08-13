package com.example.vmedvediev.ua21summerdancecamp.UI.splash

import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.vmedvediev.ua21summerdancecamp.MyApplication
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.UI.Router
import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import com.example.vmedvediev.ua21summerdancecamp.model.*
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.nio.charset.Charset

class SplashActivity : AppCompatActivity() {

    private val splashViewModel by lazy {
        ViewModelProviders.of(this, SplashViewModel(Repository(RealmEventMapper())).SplashViewModelFactory())
                .get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        when {
            EventsCache.eventsList.isNotEmpty() -> {
                startActivity(Router.prepareMainActivityIntent(this))
                finish()
            }
            splashViewModel.getEvents().isNotEmpty() -> {
                splashViewModel.setupLocalStorage()
                startActivity(Router.prepareMainActivityIntent(this))
                finish()
            }
            else -> JsonParserAsyncTask(WeakReference(this)).execute()
        }
    }

    private fun prepareEventsData(eventsList: ArrayList<Event>) {
        splashViewModel.apply {
            saveEvents(eventsList)
            setupLocalStorage()
        }
    }

    companion object {
        class JsonParserAsyncTask(private val splashActivity: WeakReference<SplashActivity>) : AsyncTask<Void, Void, ArrayList<Event>>() {

            override fun doInBackground(vararg params: Void?): ArrayList<Event> {
                Thread.sleep(2000)
                 return try {
                    val applicationInstance = MyApplication.instance
                    val applicationContext = applicationInstance.applicationContext
                    val localeLanguage = applicationContext.resources.configuration.locale.displayLanguage
                    val applicationAssets = applicationContext.assets

                    val inputStream = if (localeLanguage == MyApplication.instance.getString(R.string.label_english)) {
                        applicationAssets.open(applicationInstance.getString(R.string.list_of_events_en_json))
                     } else {
                        applicationAssets.open(MyApplication.instance.getString(R.string.list_of_events_json))
                     }
                    val size: Int = inputStream.available()
                    val buffer = ByteArray(size)
                    inputStream.apply {
                        read(buffer)
                        close()
                    }
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
                    result?.let { prepareEventsData(it) }
                    startActivity(Router.prepareMainActivityIntent(this))
                    finish()
                }
            }
        }
    }
}