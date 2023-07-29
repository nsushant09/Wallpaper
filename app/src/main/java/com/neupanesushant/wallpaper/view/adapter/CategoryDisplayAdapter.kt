package com.neupanesushant.wallpaper.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neupanesushant.wallpaper.databinding.ItemCategoryBinding

class CategoryDisplayAdapter(
    private val context: Context,
    private val categoryList : List<String>,
    private val onCategoryClick : (String) -> Unit
) :
    RecyclerView.Adapter<CategoryDisplayAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val root = binding.root
        val categoryTitle = binding.categoryTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryTitle.text = categoryList[position]
        holder.root.setOnClickListener {
            onCategoryClick(categoryList[position])
        }
    }

    override fun getItemCount(): Int = categoryList.size
}
