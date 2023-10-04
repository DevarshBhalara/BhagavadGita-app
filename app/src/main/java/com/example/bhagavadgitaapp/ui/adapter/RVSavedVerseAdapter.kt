package com.example.bhagavadgitaapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bhagavadgitaapp.databinding.ItemSavedVerseBinding
import com.example.bhagavadgitaapp.services.room.SavedSlok

class RVSavedVerseAdapter: RecyclerView.Adapter<RVSavedVerseAdapter.ViewHolder>() {

    private var savedVerse = mutableListOf<SavedSlok>()

    class ViewHolder(private val binding: ItemSavedVerseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(savedSlok: SavedSlok) {
            binding.savedVerse = savedSlok
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemSavedVerseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = savedVerse.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedVerse[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<SavedSlok>) {
        savedVerse.clear()
        savedVerse.addAll(list)
        notifyDataSetChanged()
    }
}