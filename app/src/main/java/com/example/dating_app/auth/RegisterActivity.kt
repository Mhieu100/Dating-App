package com.example.dating_app.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dating_app.MainActivity
import com.example.dating_app.R
import com.example.dating_app.databinding.ActivityLoginBinding
import com.example.dating_app.databinding.ActivityRegisterBinding
import com.example.dating_app.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import java.net.URI

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var imageUri : Uri? = null
    private var selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it

        binding.userImage.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.setOnClickListener{
            selectImage.launch("image/*")
        }

        binding.saveData.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (binding.userName.text.toString().isEmpty()
            || binding.userEmail.text.toString().isEmpty()
            || binding.userCity.text.toString().isEmpty()
            || imageUri == null
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else if (!binding.conditionTerms.isChecked) {
            Toast.makeText(this, "Please accepts term", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        var storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")


        storageRef.putFile(imageUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener{
                storageData(it)
            }.addOnFailureListener{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun storageData(imageUrl: Uri?) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            val data = UserModel(
                image = imageUrl.toString(),
                name = binding.userName.text.toString(),
                email = binding.userEmail.text.toString(),
                city = binding.userCity.text.toString(),
                number = FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString(),
                fcmToken = token
            )

            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
                .setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        Toast.makeText(this, "User register successfull", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        })
    }
}