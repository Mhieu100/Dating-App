package com.example.dating_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dating_app.activity.MessageActivity
import com.example.dating_app.databinding.ItemUserLayoutBinding
import com.example.dating_app.model.MessageModel
import com.example.dating_app.model.UserModel
import java.util.ArrayList


class DatingAdapter(val context: Context, private val list: ArrayList<UserModel>) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class DatingViewHolder(val binding: ItemUserLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context)
            , parent, false))
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {

        holder.binding.tvName.text = list[position].name
        holder.binding.tvEmail.text = list[position].email
        holder.binding.tvAddress.text = list[position].city
        holder.binding.tvAge.text = list[position].age


        Glide.with(context).load(list[position].image).into(holder.binding.userImage)

        holder.binding.chat.setOnClickListener {
            val inter = Intent(context, MessageActivity::class.java)
            inter.putExtra("userId", list[position].number)
            context.startActivity(inter)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}