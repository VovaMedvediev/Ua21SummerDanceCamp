package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.splash

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.DatabaseHelper

class FirebaseNotificationsService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID1"
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        makeNotification(p0?.let { it.notification?.let { it.body } })
    }

    private fun makeNotification(messageBody: String?) {
        if (DatabaseHelper.getApplicationSettings()?.isNotificationsEnabled!!) {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, getString(R.string.label_notification_channel), NotificationManager.IMPORTANCE_HIGH)
                channel.description = getString(R.string.label_description)
                channel.enableLights(true)
                channel.lightColor = Color.CYAN
                channel.enableVibration(true)
                channel.setSound(uri, null)
                notificationManager.createNotificationChannel(channel)
            }
            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getString(R.string.label_cherez_30_minyt))
                    .setContentText(messageBody)
                    .setSound(uri)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

            notificationManager.notify(1, builder.build())
        }
    }
}