package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import kotlinx.android.synthetic.main.fragment_settings.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository

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