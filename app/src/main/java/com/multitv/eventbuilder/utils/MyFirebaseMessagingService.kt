package com.multitv.eventbuilder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.ui.splash.activity.SplashScreen
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "New Token: $token")
        logFCMToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM Data", "Data: ${remoteMessage.data}")

        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] ?: "Notification"
            val message = remoteMessage.data["body"] ?: ""
            val imageUrl = remoteMessage.data["image"]

            if (!imageUrl.isNullOrEmpty()) {
                Log.d("FCM Data", "Image URL: $imageUrl")
                showImageNotification(title, message, imageUrl)
            } else {
                showNotification(title, message)
            }
        }

        remoteMessage.notification?.let {
            val title = it.title ?: "Notification"
            val message = it.body ?: ""
            val imageUrl = it.imageUrl?.toString()

            if (!imageUrl.isNullOrEmpty()) {
                Log.d("FCM Notification", "Image URL: $imageUrl")
                showImageNotification(title, message, imageUrl)
            } else {
                showNotification(title, message)
            }
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, SplashScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Default channel for notifications"
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(getPendingIntent())

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun showImageNotification(title: String, message: String, imageUrl: String) {
        val channelId = "default_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Image Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for image notifications"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        Thread {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                    )
                    .setAutoCancel(true)
                    .setContentIntent(getPendingIntent())

                val manager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(1, notificationBuilder.build())

            } catch (e: Exception) {
                e.printStackTrace()
                showNotification(title, message)
            }
        }.start()
    }

    private fun logFCMToken(token: String) {
        Log.d("FCM Token Log", "FCM Token: $token")

        Preference.init(applicationContext)
        Preference.saveFCMToken(token)

        // Optionally send to your server
    }
}

