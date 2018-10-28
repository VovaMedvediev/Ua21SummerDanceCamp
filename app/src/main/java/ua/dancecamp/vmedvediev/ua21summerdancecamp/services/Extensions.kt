package ua.dancecamp.vmedvediev.ua21summerdancecamp.services

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.provider.Settings
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View { return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot) }

fun Context.openLockScreenSettings() {
    val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
    startActivity(intent)
}

fun Context.openSecuritySettings() {
    val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
    startActivity(intent)
}

fun Resources.getColorCompat(id: Int, theme: Resources.Theme? = null) = ResourcesCompat.getColor(this, id, theme)

fun View.showKeyboard(delay: Long, flags: Int = 0) = postDelayed({ showKeyboard(flags) }, delay)

fun View.showKeyboard(flags: Int = 0) {
    val service: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    service?.showSoftInput(this, flags)
}