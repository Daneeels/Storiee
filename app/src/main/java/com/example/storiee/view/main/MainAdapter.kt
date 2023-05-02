package com.example.storiee.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storiee.data.response.ListStoryItem
import com.example.storiee.databinding.StoryItemRowBinding
import com.example.storiee.view.detail.DetailActivity

class MainAdapter(private var listStories: List<ListStoryItem>) : RecyclerView.Adapter<MainAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = StoryItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val story = listStories[position]

        Glide.with(holder.itemView.context).load(story.photoUrl).into(holder.binding.avatarIV)
        holder.binding.nameTV.text = story.name
        holder.binding.descTV.text = story.description

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(ID_KEY, listStories[holder.adapterPosition].id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = listStories.size

    class UserViewHolder(var binding: StoryItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val ID_KEY = "id_key"
    }
}