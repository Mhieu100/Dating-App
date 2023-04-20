package com.example.dating_app.notification.api

import com.example.dating_app.notification.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers("Content-Type:application/json",
        "Authorization:key=AAAAleYyXYs:APA91bHy8awGXuHJ90Pj6mRkl_qO_745F0vM0KOZ61ms-Ig8FE-qwySYODTDqL_UQwN4B3NSSTNF38PjUcwcR8DoX_LZN0ttqFOlivmtuW3Uiwksf-WtxrN-4mbaC6819bdsTMyB_EoN")
    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification)
    : Call<PushNotification>

}