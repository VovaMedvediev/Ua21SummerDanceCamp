package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.Fragment
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity
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

    fun startSplashScreen(fragment: Fragment) = fragment.startActivity(Intent(fragment.activity, SplashActivity::class.java))

    fun startLoginActivity(activity: Activity) = activity.startActivity(Intent(activity, LoginActivity::class.java))

    fun startMainActivity(activity: Activity) = activity.startActivity(Intent(activity, MainActivity::class.java))

    fun startNoteActivity(fragment: Fragment, eventId: String) : Intent {
        val intent = Intent(fragment.activity, NoteActivity::class.java)
        intent.putExtra(KEY_EVENT_ID_TO_NOTE_ACTIVITY, eventId)
        fragment.startActivity(intent)
        return intent
    }

    fun routeToInstagramProfile(activity: Activity, packageManager: PackageManager) : Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        var accountUrl = INSTAGRAM_ACCOUNT_URL
        try {
            if (packageManager.getPackageArchiveInfo(INSTAGRAM_PACKAGE, 0) != null) {
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
        activity.startActivity(intent)
        return intent
    }

    fun routeToTelegramProfile(activity: Activity) {
        try {
            if (activity.packageManager.getPackageInfo(TELEGRAM_X_PACKAGE, 0) != null ||
                    activity.packageManager.getPackageInfo(TELEGRAM_PACKAGE, 0) != null) {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_ACCOUNT_URL)))
            } else startMainActivity(activity)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            startMainActivity(activity)
        }
    }

    fun routeToApplicationSettings(fragment: Fragment) =
            fragment.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${fragment.activity?.packageName}")))

    fun openLockScreenSettings(activity: Activity) = activity.startActivity(Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD))

    fun openSecuritySettings(activity: Activity) = activity.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
}