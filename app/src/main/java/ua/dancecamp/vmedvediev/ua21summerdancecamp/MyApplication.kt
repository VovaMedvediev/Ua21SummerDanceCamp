package ua.dancecamp.vmedvediev.ua21summerdancecamp

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

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        Realm.init(applicationContext)
        val realmConfiguration = RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        val realmInspector = RealmInspectorModulesProvider.builder(this)
                .withDeleteIfMigrationNeeded(true)
                .build()
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(realmInspector)
                        .build())

        instance = this
    }
}