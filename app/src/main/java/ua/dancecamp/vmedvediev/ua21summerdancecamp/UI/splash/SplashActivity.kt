package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ua.dancecamp.vmedvediev.ua21summerdancecamp.MyApplication
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.Router
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.ApplicationSettings
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.Event
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.EventsCache
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository
import java.io.IOException
import java.lang.ref.WeakReference
import java.nio.charset.Charset
import java.util.*

class SplashActivity : AppCompatActivity() {

    private val splashViewModel by lazy {
        ViewModelProviders.of(this, SplashViewModel(Repository(RealmEventMapper(), RealmSettingsMapper())).SplashViewModelFactory())
                .get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel.apply {
            getApplicationSettings()
            applicationSettings.observe(this@SplashActivity, Observer {
                if (it != null) {
                    val localeLanguage = baseContext.resources.configuration.locale.language
                    val applicationLanguage = if (it.interfaceLanguage.isEmpty()) {
                        localeLanguage
                    } else {
                        it.interfaceLanguage
                    }
                    val previousLocaleLanguage = if (it.localeLanguage.isEmpty()) {
                        localeLanguage
                    } else {
                        it.localeLanguage
                    }
                    when {
                        EventsCache.eventsList.isNotEmpty() && (previousLocaleLanguage == applicationLanguage) -> {
                            startActivity(Router.prepareMainActivityIntent(this@SplashActivity))
                            finish()
                        }
                        splashViewModel.getEvents().isNotEmpty() && (previousLocaleLanguage == applicationLanguage) -> {
                            splashViewModel.setupLocalStorage()
                            startActivity(Router.prepareMainActivityIntent(this@SplashActivity))
                            finish()
                        }
                        else -> JsonParserAsyncTask(WeakReference(this@SplashActivity)).execute(applicationLanguage)
                    }
                    updateLocale(applicationLanguage)
                    splashViewModel.saveApplicationSettigns(ApplicationSettings(it.id, applicationLanguage, applicationLanguage))
                }
            })
        }
    }

    private fun updateLocale(applicationLanguage: String) {
        val locale = Locale(applicationLanguage)
        Locale.setDefault(locale)
        val config = baseContext.resources.configuration
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun prepareEventsData(eventsList: ArrayList<Event>) {
        splashViewModel.apply {
            saveEvents(eventsList)
            setupLocalStorage()
        }
    }

    companion object {
        class JsonParserAsyncTask(private val splashActivity: WeakReference<SplashActivity>) : AsyncTask<String, Void, ArrayList<Event>>() {

            override fun doInBackground(vararg params: String?): ArrayList<Event>? {
                Thread.sleep(1000)
                 return try {
                    val applicationInstance = MyApplication.instance
                    val applicationContext = applicationInstance.applicationContext
                    val applicationLanguage = params[0]
                    val applicationAssets = applicationContext.assets

                    val inputStream = when (applicationLanguage) {
                        MyApplication.instance.getString(R.string.label_english) -> applicationAssets.open(applicationInstance.getString(R.string.list_of_events_en_json))
                        MyApplication.instance.getString(R.string.label_ukranian) -> applicationAssets.open(MyApplication.instance.getString(R.string.list_of_events_ua_json))
                        else -> applicationAssets.open(MyApplication.instance.getString(R.string.list_of_events_json))
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