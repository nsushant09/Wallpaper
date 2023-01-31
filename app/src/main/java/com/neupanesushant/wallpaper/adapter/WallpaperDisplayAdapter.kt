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
import com.neupanesushant.wallpaper.R
import com.neupanesushant.wallpaper.databinding.ItemImageBinding
import com.neupanesushant.wallpaper.model.Photo

class WallpaperDisplayAdapter(
    private val context: Context,
    private val imageList: List<Photo>,
    private val onImageClick: (Photo) -> Unit
) :
    RecyclerView.Adapter<WallpaperDisplayAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        val root = binding.root
        val image = binding.displayImageIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation = AnimationUtils.loadAnimation(
            context,
            org.koin.android.R.anim.abc_grow_fade_in_from_bottom
        )
        animation.duration = 100
        holder.itemView.animation = animation
        loadImage(holder.image, 450, 480, position)

        holder.image.setOnClickListener {
            onImageClick(imageList[position])
        }
    }

    private fun loadImage(view: ImageView, width: Int, height: Int, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(imageList[position].src.portrait)
            .apply(RequestOptions.centerCropTransform())
            .apply(RequestOptions.overrideOf(width, height))
            .apply(RequestOptions().transform(RoundedCorners(32)))
            .into(view)
    }

    override fun getItemCount(): Int = imageList.size
}
