package com.example.dating_app.model

data class UserModel(
    val number: String? = "",
    val name : String? = "",
    val email : String? = "",
    val city: String? = "",
    val gender: String? = "",
    val fcmToken : String? = "",
    val image: String? = "",
    val birthday: String? = "",
    val age : String? = "",
)
