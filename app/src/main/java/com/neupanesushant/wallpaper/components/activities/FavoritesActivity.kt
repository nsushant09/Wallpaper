package com.neupanesushant.wallpaper.components.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.neupanesushant.wallpaper.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.components.viewmodels.FavoritesViewModel
import com.neupanesushant.wallpaper.databinding.ActivityFavoritesBinding
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Photo
import org.koin.android.ext.android.inject

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavoritesBinding
    private val favoritesViewModel : FavoritesViewModel by inject()

    private val onImageClick : (Photo) -> Unit = { photo ->
        val intent = Intent(this, WallpaperViewActivity::class.java)
        intent.putExtra(Constants.WALLPAPER_PHOTO, photo)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupEventListener()
        setupObserver()
    }

    private fun setupViews(){
        binding.wallpaperRv.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.notFoundTv.isVisible = false
        favoritesViewModel.getFavorites()
    }

    private fun setupEventListener(){
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObserver(){
        favoritesViewModel.favoriteImagesList.observe(this){
            if(it == null || it.isEmpty()){
                setVisibilityAccordingResult(isResultFound = false)
            }else{
                setVisibilityAccordingResult(isResultFound = true)
                val adapter = WallpaperDisplayAdapter(this, it, onImageClick)
                binding.wallpaperRv.adapter = adapter
            }
        }

        favoritesViewModel.isLoading.observe(this){
            binding.progressBar.isVisible = it
            binding.wallpaperRv.isVisible = !it
        }
    }

    private fun setVisibilityAccordingResult(isResultFound : Boolean){
        binding.wallpaperRv.isVisible = isResultFound
        binding.notFoundTv.isVisible = !isResultFound
    }

}