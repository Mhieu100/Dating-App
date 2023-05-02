package com.example.dating_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dating_app.R
import com.example.dating_app.activity.MessageActivity
import com.example.dating_app.databinding.UserItemLayoutUserBinding
import com.example.dating_app.model.UserModel
import java.util.ArrayList


class SearchAdapter(val context: Context, var list: ArrayList<UserModel>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(val binding: UserItemLayoutUserBinding)
        :RecyclerView.ViewHolder(binding.root) {
            val text = itemView.findViewById<TextView>(R.id.userName)
            val image = itemView.findViewById<ImageView>(R.id.userImage)
        }

    fun setFilterSearch( list: ArrayList<UserModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.SearchViewHolder {
        return SearchViewHolder(UserItemLayoutUserBinding.inflate(LayoutInflater.from(context)
            , parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.text.text = list[position].name
        Glide.with(context).load(list[position].image).into(holder.image)

        holder.binding.chatSearch.setOnClickListener {
            val inter = Intent(context, MessageActivity::class.java)
            inter.putExtra("userId", list[position].number)
            context.startActivity(inter)
        }
    }
    override fun getItemCount(): Int = list.size
}