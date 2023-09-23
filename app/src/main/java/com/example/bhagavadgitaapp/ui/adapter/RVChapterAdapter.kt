package com.example.bhagavadgitaapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bhagavadgitaapp.data.remote.Chapter
import com.example.bhagavadgitaapp.databinding.ItemChaptersBinding

class RVChapterAdapter: RecyclerView.Adapter<RVChapterAdapter.ViewHolder>() {

    private var chapters = mutableListOf<Chapter>()

    class ViewHolder(private val binding: ItemChaptersBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chapter: Chapter) {
            binding.chapter = chapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemChaptersBinding.inflate(LayoutInflater.from(parent.context), parent, false, null))

    override fun getItemCount(): Int =
        chapters.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chapters[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Chapter>) {
        chapters.clear()
        chapters.addAll(list)
        notifyDataSetChanged()
    }
}