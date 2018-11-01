package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.weather

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_weather.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.WeatherResponse
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class WeatherActivity : AppCompatActivity() {

    companion object {
        private const val ERROR_CODE = "400"
        private const val TEMP_DEGREES_NUMBER = 2
        private const val WIND_SPEED_NUMBER = 3
    }

    private val weatherViewModel by lazy {
        ViewModelProviders.of(this, WeatherViewModel(Repository(RealmEventMapper(),
                RealmSettingsMapper())).WeatherViewModelFactory()).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)

        weatherViewModel.apply {
            loadWeatherResponse()
            weatherResponse.observe(this@WeatherActivity, Observer {
                if (it != null && it.cod != ERROR_CODE) {
                    showWeather(it)
                } else {
                    showWeatherNotAvailable()
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

    private fun showWeather(weatherResponse: WeatherResponse) {
        val maxTemp = weatherResponse.list[0].main.maxTemp.substring(0, TEMP_DEGREES_NUMBER)
        val minTemp = weatherResponse.list[0].main.minTemp.substring(0, TEMP_DEGREES_NUMBER)
        val wind = weatherResponse.list[0].wind.speed.toString().substring(0, WIND_SPEED_NUMBER)
        maxTempTextView.text = getString(R.string.label_degrees_celsius, maxTemp)
        minTempTextView.text = getString(R.string.label_degrees_celsius, minTemp)
        windTextView.text = getString(R.string.label_m_s, wind)
    }

    private fun showWeatherNotAvailable() {
        maxTempTextView.visibility = View.GONE
        minTempTextView.visibility = View.GONE
        windTextView.visibility = View.GONE
        labelMaxTempTextView.visibility = View.GONE
        labelMinTempTextView.visibility = View.GONE
        labelWindTextView.visibility = View.GONE
        weatherNotAvailableTextView.visibility = View.VISIBLE
    }
}
