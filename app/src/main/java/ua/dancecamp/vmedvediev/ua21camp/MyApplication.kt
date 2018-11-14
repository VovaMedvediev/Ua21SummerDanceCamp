package ua.dancecamp.vmedvediev.ua21camp

import android.app.Application
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber
import timber.log.Timber.DebugTree

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
        private set
    }

    override fun onCreate() {
        super.onCreate()

        timber()

        realm()

        stetho()

        instance = this
    }

    private fun timber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private fun realm() {
        Realm.init(applicationContext)
        val realmConfiguration = RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    private fun stetho() {
        val realmInspector = RealmInspectorModulesProvider.builder(this)
                .withDeleteIfMigrationNeeded(true)
                .build()
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(realmInspector)
                        .build())
    }
}