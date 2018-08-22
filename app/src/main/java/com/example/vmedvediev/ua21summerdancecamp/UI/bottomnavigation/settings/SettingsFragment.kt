package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.UI.splash.FirebaseNotificationsService
import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.ApplicationSettings
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private val settingsViewModel by lazy {
        ViewModelProviders.of(this, SettingsViewModel(Repository(RealmEventMapper(), RealmSettingsMapper())).SettingsViewModelFactory())
                .get(SettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel.apply {
            getApplicationSettings()
            applicationSettings.observe(this@SettingsFragment, Observer {
                if (it != null) {
                    notificationsSettingsSwitch.isChecked = it.isNotificationsEnabled
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        notificationsSettingsSwitch.setOnClickListener {
//            val isNotificationsEnabled = if (notificationsSettingsSwitch.isChecked) {
//                Toast.makeText(activity as AppCompatActivity, "Оповещения включены!", Toast.LENGTH_SHORT).show()
//                true
//            } else {
//                Toast.makeText(activity as AppCompatActivity, "Оповещения выключены!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(context, FirebaseNotificationsService::class.java)
//                activity!!.stopService(intent)
//                false
//            }
//            settingsViewModel.saveApplicationSettings(ApplicationSettings("1", isNotificationsEnabled, true, ""))
//        }
    }
}