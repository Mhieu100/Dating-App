package com.example.dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.dating_app.MainActivity
import com.example.dating_app.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit
import com.example.dating_app.R
import com.example.dating_app.utils.Config
import com.example.dating_app.utils.Config.hideDialog


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    var auth = FirebaseAuth.getInstance()
    private var verificationId : String? = null
//    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout).create()

        binding.sendOTP.setOnClickListener {
            if(binding.userNumber.text!!.isEmpty()) {
                binding.userNumber.error = "Please Enter Number"
            } else {
                sendOTP(binding.userNumber.text.toString())
            }
        }

        binding.vertifyOTP.setOnClickListener {
            if(binding.userOTP.text!!.isEmpty()) {
                binding.userOTP.error = "Please Enter Number"
            } else {
                checkOtp(binding.userOTP.text.toString())
            }
        }
    }

    private fun checkOtp(otp: String) {
        Config.showDialog(this)
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun sendOTP(number: String) {
        Config.showDialog(this)
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@LoginActivity.verificationId = verificationId
                hideDialog()
                binding.cardViewNumber.visibility = GONE
                binding.cardviewOtp.visibility = VISIBLE
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+84$number")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    checkUserExist(binding.userNumber.text.toString())
                } else {
                    hideDialog()
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("users").child("+84$number")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        hideDialog()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        hideDialog()
                        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    hideDialog()
                    Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}
