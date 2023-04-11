package com.example.dating_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.dating_app.adapter.MessageUserAdapter
import com.example.dating_app.databinding.FragmentMessageBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater)

        getData()


        return binding.root
    }

    private fun getData() {

        val currentId = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<String>()
                    val newlist = arrayListOf<String>()
                    for(data in snapshot.children) {
                        if(data.key!!.contains(currentId!!)) {
                            list.add(data.key!!.replace(currentId, ""))
                            newlist.add(data.key!!)
                        }
                    }
                    try {
                        binding.recyclerView.adapter =
                            MessageUserAdapter(requireContext(), list, newlist)
                    }catch (_: Exception) {

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}