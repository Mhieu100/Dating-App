package com.example.dating_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dating_app.R
import com.example.dating_app.adapter.MessageUserAdapter
import com.example.dating_app.databinding.FragmentMessageBinding
import com.example.dating_app.ui.DatingFragment.Companion.list

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater)

        binding.recyclerView.adapter = MessageUserAdapter(requireContext(), list!!)
        return binding.root
    }
}