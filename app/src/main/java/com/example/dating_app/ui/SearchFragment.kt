package com.example.dating_app.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.dating_app.adapter.SearchAdapter
import com.example.dating_app.databinding.FragmentSearchBinding
import com.example.dating_app.model.UserModel
import com.example.dating_app.ui.DatingFragment.Companion.list
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        searchView = binding.searchView
        recyclerView = binding.recyclerViewSearch



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
        getData()
        return binding.root
    }

    private fun filterList(query: String?) {
        if(query != null) {
            val filterList = ArrayList<UserModel>()
            for(i in list!!) {
                if(i.name!!.lowercase(Locale.ROOT).contains(query)) {
                    filterList.add(i)
                }
            }
            if(filterList.isEmpty()) {
                Toast.makeText(context, "Not Found Data", Toast.LENGTH_SHORT).show()
            } else {
                adapter = SearchAdapter(requireContext(), list!!)
                recyclerView.adapter = adapter
                adapter.setFilterSearch(filterList)
            }
        }
    }

    companion object {
        var list: java.util.ArrayList<UserModel>? = null
    }

    private fun getData() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("users")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Lấy dữ liệu từ snapshot và đưa vào danh sách

                list = arrayListOf()
                for (data in snapshot.children) {
                    val model = data.getValue(UserModel::class.java)
                    if (model!!.number != FirebaseAuth.getInstance().currentUser!!.phoneNumber) {
                        list!!.add(model)
                    }
                }
                // Hiển thị dữ liệu lên RecyclerView
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = LinearLayoutManager(context)
                adapter = SearchAdapter(context!!, list!!)
                recyclerView.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        })
    }
}