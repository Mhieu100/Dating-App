package com.example.dating_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dating_app.databinding.ItemUserLayoutBinding
import com.example.dating_app.model.UserModel
import java.util.ArrayList

class DatingAdapter(val context: Context, val list: ArrayList<UserModel>) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class DatingViewHolder(val binding: ItemUserLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {


        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context)
            , parent, true))
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        holder.binding.textView8.text = list[position].name
        holder.binding.textView7.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.userImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}