package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.Fragment
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.weather.WeatherActivity
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes.NoteActivity
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes.NoteActivity.Companion.KEY_EVENT_ID_TO_NOTE_ACTIVITY
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.login.LoginActivity
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.splash.SplashActivity

object Router {

    private const val INSTAGRAM_ACCOUNT_URL = "https://www.instagram.com/dancecampua21/"
    private const val TELEGRAM_ACCOUNT_URL = "tg://resolve?domain=ua21sdc"
    private const val INSTAGRAM_PACKAGE = "com.instagram.android"
    private const val TELEGRAM_X_PACKAGE = "org.thunderdog.challegram"
    private const val TELEGRAM_PACKAGE = "org.telegram.messenger"

    fun startSplashScreen(activity: Activity?) {
        activity?.startActivity(Intent(activity, SplashActivity::class.java))
    }

    fun startLoginActivity(activity: Activity?) {
        activity?.startActivity(Intent(activity, LoginActivity::class.java))
    }

    fun startMainActivity(activity: Activity?) {
        activity?.startActivity(Intent(activity, MainActivity::class.java))
    }

    fun startNoteActivity(activity: Activity?, eventId: String): Intent {
        val intent = Intent(activity, NoteActivity::class.java)
        intent.putExtra(KEY_EVENT_ID_TO_NOTE_ACTIVITY, eventId)
        activity?.startActivity(intent)
        return intent
    }

    fun routeToInstagramProfile(activity: Activity?): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        var accountUrl = INSTAGRAM_ACCOUNT_URL
        activity?.let {
            try {
                if (it.packageManager.getPackageArchiveInfo(INSTAGRAM_PACKAGE, 0) != null) {
                    if (accountUrl.endsWith("/")) {
                        accountUrl = accountUrl.substring(0, INSTAGRAM_ACCOUNT_URL.length - 1)
                    }
                    val username = accountUrl.substring(INSTAGRAM_ACCOUNT_URL.lastIndexOf(("/") + 1))
                    intent.data = Uri.parse("http://instagram.com/_u/$username")
                    intent.setPackage(INSTAGRAM_PACKAGE)
                    return intent
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            intent.data = Uri.parse(accountUrl)
            it.startActivity(intent)
        }
        return intent
    }

    fun routeToTelegramProfile(activity: Activity?) {
        activity?.let {
            try {
                if (it.packageManager.getPackageInfo(TELEGRAM_X_PACKAGE, 0) != null ||
                        it.packageManager.getPackageInfo(TELEGRAM_PACKAGE, 0) != null) {
                    it.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_ACCOUNT_URL)))
                } else startMainActivity(it)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                startMainActivity(it)
            }
        }
    }

    fun routeToApplicationSettings(activity: Activity?) {
        activity?.let {
            it.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${it.packageName}")))
        }
    }

    fun openLockScreenSettings(activity: Activity?) {
        activity?.startActivity(Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD))
    }

    fun openSecuritySettings(activity: Activity?) {
        activity?.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
    }

    fun prepareWeatherActivityIntent(activity: Activity?) {
        activity?.startActivity(Intent(activity, WeatherActivity::class.java))
    }
}