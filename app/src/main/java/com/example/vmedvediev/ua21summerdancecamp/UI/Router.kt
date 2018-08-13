package com.example.vmedvediev.ua21summerdancecamp.UI

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes.NoteActivity
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes.NoteActivity.Companion.KEY_EVENT_ID_TO_NOTE_ACTIVITY

object Router {

    const val INSTAGRAM_ACCOUNT_URL = "https://www.instagram.com/dancecampua21/"
    const val TELEGRAM_ACCOUNT_URL = "tg://resolve?domain=groupname"
    const val INSTAGRAM_PACKAGE = "com.instagram.android"

    fun prepareMainActivityIntent(context: Context) = Intent(context, MainActivity::class.java)

    fun prepareNoteActivityIntent(context: Context, eventId: String) : Intent {
        val intent = Intent(context, NoteActivity::class.java)
        intent.putExtra(KEY_EVENT_ID_TO_NOTE_ACTIVITY, eventId)
        return intent
    }

    fun prepareInstagramProfileIntent(packageManager: PackageManager) : Intent {
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
        return intent
    }

    fun prepareTelegramProfileIntent() = Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_ACCOUNT_URL ))
}