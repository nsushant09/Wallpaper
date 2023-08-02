package com.neupanesushant.wallpaper.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.neupanesushant.wallpaper.databinding.ItemCategoryRvBinding
import com.neupanesushant.wallpaper.domain.model.KeyValue

class CategoryRvDisplayAdapter(
    private val context: Context,
    private val list: List<KeyValue>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryRvDisplayAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = list[position]
        holder.binding.displayTitleTv.text = obj.key

        holder.binding.root.setOnClickListener {
            onClick(obj.key)
        }
        Glide.with(context)
            .load(obj.value)
            .apply(RequestOptions().transform(RoundedCorners(32)))
            .into(holder.binding.displayImageIv)

    }

}