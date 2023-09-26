package com.example.bhagavadgitaapp.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bhagavadgitaapp.data.local.TranslationOrCommentary
import com.example.bhagavadgitaapp.databinding.ItemTranslationBinding

class RVNestedVerseTranslationAdapter: RecyclerView.Adapter<RVNestedVerseTranslationAdapter.ViewHolder>() {

    private var translations = mutableListOf<TranslationOrCommentary>()

    class ViewHolder(private val binding: ItemTranslationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(translationOrCommentary: TranslationOrCommentary) {
            Log.e("log", translationOrCommentary.toString())
            binding.translations = translationOrCommentary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemTranslationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = translations.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("data", translations[position].toString())
        holder.bind(translations[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<TranslationOrCommentary>) {
        translations.clear()
        translations.addAll(list)
        notifyDataSetChanged()
    }
}