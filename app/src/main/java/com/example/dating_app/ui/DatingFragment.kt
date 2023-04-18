package com.example.dating_app.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.dating_app.adapter.DatingAdapter
import com.example.dating_app.databinding.FragmentDatingBinding
import com.example.dating_app.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class DatingFragment : Fragment() {

    private lateinit var binding: FragmentDatingBinding
    private lateinit var manager: CardStackLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatingBinding.inflate(layoutInflater)

        getData()
        return binding.root
    }

    private fun init() {
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
                if (manager.topPosition == list!!.size) {
//                    Toast.makeText(requireContext(), "This is last card", Toast.LENGTH_SHORT).show()
                    getData()
                }
            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
    }


    companion object {
        var list: java.util.ArrayList<UserModel>? = null
    }

    private fun getData() {
        var genderYou: String = ""
        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val data1 = it.getValue(UserModel::class.java)
                    genderYou = data1!!.gender.toString()
                }
            }

        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("SHUBHA", "onDataChange: ${snapshot.toString()}")
                    if (snapshot.exists()) {
                        list = arrayListOf()
                        for (data in snapshot.children) {
                            val model = data.getValue(UserModel::class.java)
                            if (genderYou != model!!.gender && model.number != FirebaseAuth.getInstance().currentUser!!.phoneNumber) {
                                list!!.add(model)
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                    list!!.shuffle()
                    init()
                    binding.cardStackView.layoutManager = manager
                    binding.cardStackView.itemAnimator = DefaultItemAnimator()
                    binding.cardStackView.adapter = DatingAdapter(requireContext(), list!!)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}