package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import kotlinx.android.synthetic.main.fragment_settings.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.Router
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmCredentialsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.ApplicationSettings
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

class SettingsFragment : Fragment() {

    companion object {
        private const val UKRAINIAN_LANGUAGE_TAG = "ua"
        private const val RUSSIAN_LANGUAGE_TAG = "ru"
        private const val ENGLISH_LANGUAGE_TAG = "en"
        private const val APPLICATION_SETTINGS_ID = "1"
    }

    private val settingsViewModel by lazy {
        ViewModelProviders.of(this, SettingsViewModel(Repository(RealmEventMapper(),
                RealmSettingsMapper(), RealmCredentialsMapper())).SettingsViewModelFactory())
                .get(SettingsViewModel::class.java)
    }
    private var interfaceLanguage = ""
    private val localeLanguage = if (activity != null) {
        (activity as AppCompatActivity).baseContext.resources.configuration.locale.language
    } else {
        ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onStart() {
        super.onStart()
        settingsViewModel.apply {
            getApplicationSettings()
            applicationSettings.observe(this@SettingsFragment, Observer {
                if (it != null) {
                    when (it.interfaceLanguage) {
                        UKRAINIAN_LANGUAGE_TAG -> chooseLanguageRadioGroup.check(R.id.ukranianRadioButton)
                        RUSSIAN_LANGUAGE_TAG -> chooseLanguageRadioGroup.check(R.id.russianRadioButton)
                        ENGLISH_LANGUAGE_TAG -> chooseLanguageRadioGroup.check(R.id.englishRadioButton)
                    }
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = context
        notificationsSettingsButton.setOnClickListener {
            if (context != null) {
                Router.routeToApplicationSettings(this.activity)
            }
        }

        chooseLanguageRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.ukranianRadioButton -> interfaceLanguage = UKRAINIAN_LANGUAGE_TAG
                R.id.russianRadioButton -> interfaceLanguage = RUSSIAN_LANGUAGE_TAG
                R.id.englishRadioButton -> interfaceLanguage = ENGLISH_LANGUAGE_TAG
            }
        }

        chooseLanguageButton.setOnClickListener {
            if (localeLanguage != null) {
                settingsViewModel.saveApplicationSettings(ApplicationSettings(APPLICATION_SETTINGS_ID, interfaceLanguage, localeLanguage))
            }
            if (context != null) {
                Router.startSplashScreen(this.activity)
                activity?.finish()
            }
        }
    }
}