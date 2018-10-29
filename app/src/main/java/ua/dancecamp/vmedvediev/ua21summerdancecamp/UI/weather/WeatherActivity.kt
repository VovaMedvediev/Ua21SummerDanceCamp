package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.weather

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_weather.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class WeatherActivity : AppCompatActivity() {

    private val weatherViewModel by lazy {
        ViewModelProviders.of(this, WeatherViewModel(Repository(RealmEventMapper(),
                RealmSettingsMapper())).WeatherViewModelFactory()).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)

        weatherViewModel.apply {
            getWeatherResponse()
            weatherResponse.observe(this@WeatherActivity, Observer {
                if (it != null) {
                    val maxTemp = it.list[0].main.maxTemp.substring(0, 2)
                    val minTemp = it.list[0].main.minTemp.substring(0, 2)
                    val wind = it.list[0].wind.speed.toString().substring(0, 3)
                    maxTempTextView.text = getString(R.string.label_degrees_celsius, maxTemp)
                    minTempTextView.text = getString(R.string.label_degrees_celsius, minTemp)
                    windTextView.text = getString(R.string.label_m_s, wind)
                }
            })
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
            toolbarTitleTextView.text = getString(R.string.label_current_weather)
        }
    }
}
