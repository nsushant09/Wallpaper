package com.neupanesushant.wallpaper.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neupanesushant.wallpaper.databinding.ItemSliderImageBinding
import com.neupanesushant.wallpaper.domain.model.KeyValue

class ImageSliderAdapter(
    private val context: Context,
    private val list: ArrayList<KeyValue>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ImageSliderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSliderImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.categoryImage
        val title = binding.categoryTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSliderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].key
        loadImage(holder.image, position)
        holder.binding.root.setOnClickListener {
            onClick(list[position].key)
        }
    }

    private fun loadImage(view: ImageView, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(list[position].value)
            .into(view)
    }
}