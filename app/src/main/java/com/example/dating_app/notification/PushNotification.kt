package com.example.dating_app.notification

data class PushNotification (
    val data: NotificationData,
    val to : String ? = ""
)