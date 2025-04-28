package com.epsports.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("TAG", "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val body = message.notification?.body

        Log.d("TAG", "onMessageReceived: $title")
        Log.d("TAG", "onMessageReceived: $body")

        showNotification(title, body)
    }

    private fun showNotification(title: String?, body: String?) {
        var channel: NotificationChannel? = null
        var builder: NotificationCompat.Builder? = null

        val channelId = "com.epsports.pushnotification.test-notification"

        val manager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this@MyService, MainActivity2::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this@MyService,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this@MyService, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.baseline_notifications_none_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            builder = NotificationCompat.Builder(this@MyService)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.baseline_notifications_none_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        val id = System.currentTimeMillis().toInt()
        manager.notify(id, builder.build())

    }
}