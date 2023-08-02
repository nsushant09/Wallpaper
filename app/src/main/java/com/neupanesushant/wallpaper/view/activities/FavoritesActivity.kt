package com.neupanesushant.wallpaper.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.neupanesushant.wallpaper.databinding.ActivityFavoritesBinding
import com.neupanesushant.wallpaper.domain.model.Constants
import com.neupanesushant.wallpaper.domain.model.Photo
import com.neupanesushant.wallpaper.view.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.view.viewmodels.FavoritesViewModel
import org.koin.android.ext.android.inject

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val favoritesViewModel: FavoritesViewModel by inject()
    private lateinit var adapter: WallpaperDisplayAdapter

    private val onImageClick: (Photo) -> Unit = { photo ->
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

    private fun setupViews() {
        binding.wallpaperRv.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.notFoundTv.isVisible = false
        favoritesViewModel.getFavorites()
    }

    private fun setupEventListener() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObserver() {
        favoritesViewModel.favoriteImagesList.observe(this) {

            setVisibilityAccordingResult(it.isNotEmpty())

            if (this@FavoritesActivity::adapter.isInitialized) {
                adapter.changeList(it)
            } else {
                adapter = WallpaperDisplayAdapter(this, it, onImageClick)
                binding.wallpaperRv.adapter = adapter
            }
        }

        favoritesViewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
            binding.wallpaperRv.isVisible = !it
        }
    }

    private fun setVisibilityAccordingResult(isResultFound: Boolean) {
        binding.wallpaperRv.isVisible = isResultFound
        binding.notFoundTv.isVisible = !isResultFound
    }

}