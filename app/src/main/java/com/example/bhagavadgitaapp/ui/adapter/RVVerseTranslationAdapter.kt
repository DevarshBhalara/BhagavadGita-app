package com.example.bhagavadgitaapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bhagavadgitaapp.data.local.Authors
import com.example.bhagavadgitaapp.databinding.ItemVerseBinding
import com.example.bhagavadgitaapp.listners.ItemClickListener

class RVVerseTranslationAdapter: RecyclerView.Adapter<RVVerseTranslationAdapter.ViewHolder>() {

    private var authors = mutableListOf<Authors>()
    var itemClickListener: ItemClickListener<Authors>? = null

    inner class ViewHolder(private val itemVerseBinding: ItemVerseBinding): RecyclerView.ViewHolder(itemVerseBinding.root) {
        private val adapter = RVNestedVerseTranslationAdapter()
        fun bind(author: Authors, position: Int) {
            itemVerseBinding.author = author
            itemVerseBinding.rvTranslation.adapter = adapter
            adapter.submitList(author.translation)
            itemVerseBinding.btnExpand.setOnClickListener {
                expandOrCollapse(position)
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun expandOrCollapse(position: Int) {
            itemVerseBinding.root.setOnClickListener {
               itemClickListener?.onClick(authors[position], position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemVerseBinding.inflate(LayoutInflater.from(parent.context), parent, false, null)
        )

    override fun getItemCount(): Int =
        authors.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(authors[position], position)
        holder.expandOrCollapse(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Authors>) {
        authors.clear()
        authors.addAll(list)
        notifyDataSetChanged()
    }
}