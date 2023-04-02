package com.example.dating_app.notification.api

import com.example.dating_app.notification.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers("Content-Type:application/json",
        "Authorization:key=AAAAleYyXYs:APA91bFP7lhlX37HimCTlk3ISYxTjkqo2HGmTmjvry5vs6g4HUqvoRNlRJPOAs1axisMfYk_1xHOTG2xlYMruCuvt7QvKvGNBGogRoiP8NdKYQJFJMztK03b70JqywxNaZMV6Cgmk-Eu")
    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification)
    : Call<PushNotification>

}