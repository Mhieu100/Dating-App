package com.example.dating_app.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dating_app.MainActivity
import com.example.dating_app.R
import com.example.dating_app.databinding.ActivityRegisterBinding
import com.example.dating_app.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextDate: EditText
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

        editTextDate = binding.birthday
        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                editTextDate.setText(dateFormat.format(calendar.time))
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
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
        val storageRef = FirebaseStorage.getInstance().getReference("profile")
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

            val radio = binding.radio
            val radioId = radio.checkedRadioButtonId
            val radioBtn = findViewById<RadioButton>(radioId)
            val genderSelect = radioBtn.text.toString()


            val data = UserModel(
                image = imageUrl.toString(),
                name = binding.userName.text.toString(),
                email = binding.userEmail.text.toString(),
                city = binding.userCity.text.toString(),
                number = FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString(),
                fcmToken = token,
                gender = genderSelect

            )

            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
                .setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        Toast.makeText(this, "User register successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        })
    }
}