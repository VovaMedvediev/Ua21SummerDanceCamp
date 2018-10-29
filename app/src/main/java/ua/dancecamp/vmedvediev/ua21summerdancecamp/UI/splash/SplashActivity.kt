package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.hardware.fingerprint.FingerprintManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import ua.dancecamp.vmedvediev.ua21summerdancecamp.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.Router
import ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication.AuthenticationDialog
import ua.dancecamp.vmedvediev.ua21summerdancecamp.authentication.EncryptionServices
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmCredentialsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.services.SystemServices
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
        ViewModelProviders.of(this, SplashViewModel(Repository(RealmEventMapper(),
                RealmSettingsMapper(), RealmCredentialsMapper())).SplashViewModelFactory())
                .get(SplashViewModel::class.java)
    }
    private val systemServices by lazy(LazyThreadSafetyMode.NONE) { SystemServices(this) }
    private var deviceSecurityAlert: AlertDialog? = null
    private var shouldLoginScreenBeOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        if (systemServices.isDeviceSecure()) {
            splashViewModel.apply {
                getCredentials()
                credentials.observe(this@SplashActivity, Observer { credentials ->
                    if (credentials != null) {
                        if (credentials.password.isNotEmpty()) {
                            val dialog = AuthenticationDialog()
                                dialog.apply {
                                    if (credentials.isFingerPrintAllowed && systemServices.hasEnrolledFingerprints()) {
                                    cryptoObjectToAuthenticateWith = EncryptionServices(applicationContext).prepareFingerprintCryptoObject()
                                    fingerprintInvalidationListener = { onFingerprintInvalidation(it) }
                                    fingerprintAuthenticationSuccessListener = { validateKeyAuthentication(it) }
                                    if (this.cryptoObjectToAuthenticateWith == null) this.stage =
                                            AuthenticationDialog.Stage.NEW_FINGERPRINT_ENROLLED else dialog.stage = AuthenticationDialog.Stage.FINGERPRINT
                                } else {
                                        this.stage = AuthenticationDialog.Stage.PASSWORD
                                    }
                                    authenticationSuccessListener = { checkSettings() }
                                    passwordVerificationListener = { validatePassword(it) }
                                    show(supportFragmentManager, "Authentication")
                            }
                        } else {
                            shouldLoginScreenBeOpened = true
                            checkSettings(shouldLoginScreenBeOpened)
                        }
                    }
                })
            }
        } else {
            deviceSecurityAlert = systemServices.showDeviceSecurityAlert()
        }
    }

    override fun onStop() {
        super.onStop()
        deviceSecurityAlert?.dismiss()
    }

    private fun onFingerprintInvalidation(useInFuture: Boolean) {
        splashViewModel.credentials.value?.isFingerPrintAllowed = useInFuture
        if (useInFuture) {
            EncryptionServices(applicationContext).createFingerprintKey()
        }
    }

    private fun validateKeyAuthentication(cryptoObject: FingerprintManager.CryptoObject) {
        if (EncryptionServices(applicationContext).validateFingerprintAuthentication(cryptoObject)) {
            checkSettings()
        }
    }

    private fun validatePassword(inputtedPassword: String): Boolean {
        val savedPassword = splashViewModel.credentials.value?.password
        Timber.e("INPUT: $inputtedPassword +++ SAVED: $savedPassword")
        return if (savedPassword != null) {
            EncryptionServices(applicationContext).decrypt(savedPassword, inputtedPassword) == inputtedPassword
        } else {
            false
        }
    }

    private fun checkSettings(shouldLoginScreenBeOpened: Boolean = false) {
        splashViewModel.apply {
            getApplicationSettings()
            applicationSettings.observe(this@SplashActivity, Observer {
                if (it != null) {
                    val applicationLanguage  = checkApplicationLanguage(it)
                    val previousLocaleLanguage = checkPreviousLocaleLanguage(it)

                    startMainActivity(previousLocaleLanguage, applicationLanguage, shouldLoginScreenBeOpened)

                    updateLocale(applicationLanguage)
                    splashViewModel.saveApplicationSettings(ApplicationSettings(it.id, applicationLanguage, applicationLanguage))
                }
            })
        }
    }

    private fun startMainActivity(previousLocaleLanguage: String, applicationLanguage: String,
                                  shouldLoginScreenBeOpened: Boolean) {
        when {
            EventsCache.eventsList.isNotEmpty() && (previousLocaleLanguage == applicationLanguage) -> {
                navigateToNextScreen(shouldLoginScreenBeOpened)
                finish()
            }
            splashViewModel.getEvents().isNotEmpty() && (previousLocaleLanguage == applicationLanguage) -> {
                splashViewModel.setupLocalStorage()
                navigateToNextScreen(shouldLoginScreenBeOpened)
                finish()
            }
            else -> JsonParserAsyncTask(WeakReference(this@SplashActivity)).execute(applicationLanguage)
        }
    }

    private fun navigateToNextScreen(shouldLoginScreenBeOpened: Boolean) {
        if (!shouldLoginScreenBeOpened) {
            startActivity(Router.prepareMainActivityIntent(this@SplashActivity))
        } else {
            startActivity(Router.prepareLoginActivityIntent(this))
        }
    }

    private fun checkApplicationLanguage(applicationSettings: ApplicationSettings) : String {
        val localeLanguage = baseContext.resources.configuration.locale.language
        return if (applicationSettings.interfaceLanguage.isEmpty()) {
            localeLanguage
        } else {
            applicationSettings.interfaceLanguage
        }
    }

    private fun checkPreviousLocaleLanguage(applicationSettings: ApplicationSettings) : String {
        val localeLanguage = baseContext.resources.configuration.locale.language
        return if (applicationSettings.localeLanguage.isEmpty()) {
            localeLanguage
        } else {
            applicationSettings.localeLanguage
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
                        applicationInstance.getString(R.string.label_english) -> applicationAssets.open(applicationInstance.getString(R.string.list_of_events_en_json))
                        applicationInstance.getString(R.string.label_ukranian) -> applicationAssets.open(MyApplication.instance.getString(R.string.list_of_events_ua_json))
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
                    navigateToNextScreen(shouldLoginScreenBeOpened)
                    finish()
                }
            }
        }
    }
}