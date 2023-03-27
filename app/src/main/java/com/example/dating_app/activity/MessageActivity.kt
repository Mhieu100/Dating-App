package com.example.dating_app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dating_app.adapter.MessageAdpater
import com.example.dating_app.databinding.ActivityMessageBinding
import com.example.dating_app.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData(intent.getStringExtra("chat_id"))

        binding.sendMess.setOnClickListener{
            if(binding.youMessage.text!!.isEmpty()) {
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT)
            } else {
                sendMessage(binding.youMessage.text.toString())
            }
        }
    }

    private fun getData(chat_Id: String?) {
        FirebaseDatabase.getInstance().getReference("chats")
            .child(chat_Id!!).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<MessageModel>()

                    for(show in snapshot.children) {
                        list.add(show.getValue(MessageModel::class.java)!!)
                    }

                    binding.recyclerView2.adapter = MessageAdpater(this@MessageActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MessageActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun sendMessage(msg: String) {
        val receiverId = intent.getStringExtra("userId")
        val senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        val chatId = senderId + receiverId
        val currentDate : String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime : String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["senderId"] = senderId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate

        val reference =   FirebaseDatabase.getInstance().getReference("chats").child(chatId)


        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
                if(it.isSuccessful){
                    binding.youMessage.text = null
                    Toast.makeText(this, "Message Sended", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }
}