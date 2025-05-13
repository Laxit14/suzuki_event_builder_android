package com.multitv.eventbuilder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotificationManager(private val context: Context) {

    // Method to log the FCM token
    fun logFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Log the FCM token
                val token = task.result
                logTokenToConsole(token)
            } else {
                Log.d("FCM", "Fetching FCM token failed", task.exception)
            }
        }
    }

    // Method to log the FCM token to console (useful for debugging)
    private fun logTokenToConsole(token: String?) {
        token?.let {
            Log.d("FCM Token", "Token: $it")
        }
    }

    // Method to send a test notification (this can be done via the Firebase Console)
    fun sendTestNotification(title: String, message: String) {
        // Create notification channel for devices running Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel"
            val channelName = "Default Channel"
            val channelDescription = "Notification channel for default notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = channelDescription
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, "default_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        // Show the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}
