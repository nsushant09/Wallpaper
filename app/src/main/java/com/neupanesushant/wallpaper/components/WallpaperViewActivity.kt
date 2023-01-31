package com.neupanesushant.wallpaper.components

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.neupanesushant.wallpaper.databinding.ActivityWallpaperViewBinding
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.showText

class WallpaperViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWallpaperViewBinding
    private lateinit var wallpaperPhoto: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getParcelableExtra<Photo>(Constants.WALLPAPER_PHOTO) == null) {
            finish()
            this.showText("Shit happened")
        } else {
            wallpaperPhoto = intent.getParcelableExtra<Photo>(Constants.WALLPAPER_PHOTO)!!
        }

        setupView()
        setupEventListener()
        setupObserver()
    }

    private fun setupView() {
        Glide.with(this).load(wallpaperPhoto.src.large2x).apply(
            RequestOptions().transform(
                RoundedCorners(64)
            )
        ).into(binding.wallpaperImage)
    }

    private fun setupEventListener() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnInfo.setOnClickListener {

        }
    }

    private fun setupObserver() {

    }
}