package com.neupanesushant.wallpaper.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.neupanesushant.wallpaper.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.databinding.ActivityMainBinding
import com.neupanesushant.wallpaper.model.Photo
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by inject()
    private val onImageClick : (Photo) -> Unit = {photo ->

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupEventListener()
        setupObservers()
    }

    private fun setupViews(){

        mainViewModel.getRandomImages()
        binding.wallpaperRv.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setupEventListener(){
        binding.btnCategory.setOnClickListener {

        }

        binding.btnCollection.setOnClickListener {

        }

        binding.btnSearch.setOnClickListener {

        }
    }

    private fun setupObservers(){
        mainViewModel.imageResponse.observe(this) {
            val rvWallpaperAdapter = WallpaperDisplayAdapter(this, it.photos, onImageClick)
            binding.wallpaperRv.adapter = rvWallpaperAdapter
        }
    }

}