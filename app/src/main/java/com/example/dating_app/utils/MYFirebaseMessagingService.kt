package com.example.dating_app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.dating_app.MainActivity
import com.example.dating_app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MYFirebaseMessagingService : FirebaseMessagingService() {

    private val channelId = "Chat-Dating-App"

    override fun  OnMessageReceived(message : RemoteMessage) {
        super.onMessageReceived(message)
        val inter = Intent(this, MainActivity::class.java)
        inter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)

        val inter_1 = PendingIntent.getActivities(this, 0, arrayOf(inter), PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.notification)
            .setAutoCancel(true)
            .setContentIntent(inter_1)
            .build()
        manager.notify(Random.nextInt(), notification)
    }

    private fun createNotificationChannel(manager: NotificationManager) {

        val channel = NotificationChannel(channelId, "chat_room_server", NotificationManager.IMPORTANCE_HIGH)

        channel.description = "New Chat"
        channel.enableLights(true)

        manager.createNotificationChannel(channel)
    }
}