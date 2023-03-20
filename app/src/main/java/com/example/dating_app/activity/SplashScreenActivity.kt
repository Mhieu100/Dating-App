package com.example.dating_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.dating_app.MainActivity
import com.example.dating_app.R
import com.example.dating_app.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var user = FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (user == null)
                startActivity(Intent(this, LoginActivity::class.java))
            else
                startActivity(Intent(this, MainActivity::class.java))

        }, 2000)
    }
}

