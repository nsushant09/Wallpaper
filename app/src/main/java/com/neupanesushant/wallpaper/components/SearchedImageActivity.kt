package com.neupanesushant.wallpaper.components

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.neupanesushant.wallpaper.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.components.viewmodels.SearchedImageViewModel
import com.neupanesushant.wallpaper.databinding.ActivitySearchedImageBinding
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.showText
import org.koin.android.ext.android.inject
import java.util.*

class SearchedImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchedImageBinding
    private lateinit var searchQuery: String
    private val searchedImageViewModel : SearchedImageViewModel by inject()
    private val onImageClick : (Photo) -> Unit = { photo ->
        val intent = Intent(this, WallpaperViewActivity::class.java)
        intent.putExtra(Constants.WALLPAPER_PHOTO, photo)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.getStringExtra(Constants.SEARCH_QUERY) == null) {
            finish()
            this.showText("Shit Happened")
        } else {
            searchQuery = intent.getStringExtra(Constants.SEARCH_QUERY)!!
        }

        setupView()
        setupEventListeners()
        setupObservers()
    }

    private fun setupView() {
        binding.searchTitle.text = searchQuery.capitalize(Locale.ROOT)
        binding.wallpaperRv.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.notFoundTv.isVisible = false

        searchedImageViewModel.getSearchImages(searchQuery)

    }

    private fun setupEventListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObservers() {
        searchedImageViewModel.searchedImageResponse.observe(this){
            if(it == null || it.photos.isEmpty()){
                setVisibilityAccordingResult(isResultFound = false)
            }else{
                setVisibilityAccordingResult(isResultFound = true)
                val adapter = WallpaperDisplayAdapter(this, it.photos, onImageClick)
                binding.wallpaperRv.adapter = adapter
            }
        }

        searchedImageViewModel.isLoading.observe(this){
            binding.progressBar.isVisible = it
            binding.wallpaperRv.isVisible = !it
        }
    }

    private fun setVisibilityAccordingResult(isResultFound : Boolean){
        binding.wallpaperRv.isVisible = isResultFound
        binding.notFoundTv.isVisible = !isResultFound
    }

}