package com.neupanesushant.wallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.neupanesushant.wallpaper.components.BottomSheetCallback
import com.neupanesushant.wallpaper.components.CategoryBottomSheet
import com.neupanesushant.wallpaper.databinding.ItemCategoryBinding
import com.neupanesushant.wallpaper.databinding.ItemImageBinding
import com.neupanesushant.wallpaper.model.Photo

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
