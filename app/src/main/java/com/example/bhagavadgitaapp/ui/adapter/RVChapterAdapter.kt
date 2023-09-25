package com.example.bhagavadgitaapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bhagavadgitaapp.data.remote.Chapter
import com.example.bhagavadgitaapp.databinding.ItemChaptersBinding
import com.example.bhagavadgitaapp.listners.ItemClickListener

class RVChapterAdapter: RecyclerView.Adapter<RVChapterAdapter.ViewHolder>() {

    private var chapters = mutableListOf<Chapter>()
    var itemClickListener: ItemClickListener<Chapter>? = null

    inner class ViewHolder(private val binding: ItemChaptersBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                itemClickListener?.onClick(
                    chapters[adapterPosition],
                    adapterPosition
                )
            }
        }

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