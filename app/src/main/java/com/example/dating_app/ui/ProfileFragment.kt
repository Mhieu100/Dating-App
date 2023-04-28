package com.example.dating_app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.dating_app.MainActivity
import com.example.dating_app.R
import com.example.dating_app.auth.LoginActivity
import com.example.dating_app.databinding.FragmentProfileBinding
import com.example.dating_app.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var genderSelect : String
    private lateinit var image : String
    private var imageUri : Uri? = null
    private var selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.userImage.setImageURI(imageUri)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val data = it.getValue(UserModel::class.java)

                    binding.name.setText(data!!.name.toString())
                    binding.email.setText(data.email.toString())
                    binding.phone.setText(data.number.toString())
                    binding.address.setText(data.city.toString())
                    genderSelect = data.gender.toString()
                    image = data.image.toString()
                    val img = data.image
                    Glide.with(this).load(img).placeholder(R.drawable.profilecl).into(binding.userImage)
                }
            }
        binding.logOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
        binding.editProfile.setOnClickListener{
            validateData()
        }
        binding.userImage.setOnClickListener{
            selectImage.launch("image/*")
        }
        return binding.root
    }

    private fun validateData() {
        if (binding.name.text.toString().isEmpty()
            || binding.email.text.toString().isEmpty()
            || binding.address.text.toString().isEmpty()
        ) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }  else if (imageUri == null) {
            storageDataNoImage()
        } else {
            uploadImage()
        }
    }
    private fun storageDataNoImage() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            val data = UserModel(
                image = image,
                name = binding.name.text.toString(),
                email = binding.email.text.toString(),
                city = binding.address.text.toString(),
                number = FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString(),
                fcmToken = token,
                gender = genderSelect

            )

            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
                .setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "User register successfull", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), it.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        })
    }

    private fun uploadImage() {
        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")


        storageRef.putFile(imageUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener{
                storageData(it)
            }.addOnFailureListener{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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
                name = binding.name.text.toString(),
                email = binding.email.text.toString(),
                city = binding.address.text.toString(),
                number = FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString(),
                fcmToken = token,
                gender = genderSelect

            )

            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
                .setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "User register successfull", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), it.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        })
    }
}