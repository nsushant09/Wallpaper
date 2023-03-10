package com.neupanesushant.wallpaper.components.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.neupanesushant.wallpaper.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.components.extras.DialogUtils
import com.neupanesushant.wallpaper.components.extras.SystemServiceManagers
import com.neupanesushant.wallpaper.components.viewmodels.ResponseCacheViewModel
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
    private val searchedImageViewModel: SearchedImageViewModel by inject()
    private val cacheViewModel: ResponseCacheViewModel by inject()
    private val onImageClick: (Photo) -> Unit = { photo ->
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
        binding.wallpaperRv.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.notFoundTv.isVisible = false

        cacheViewModel.getSearchResponse(searchQuery)
    }

    private fun setupEventListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObservers() {
        searchedImageViewModel.searchedImageResponse.observe(this) {
            setVisibilityAccordingResult(!(it == null || it.photos.isEmpty()))
            it?.let {
                setupWallpaperRv(it.photos)
                cacheViewModel.addSearchResponse(searchQuery, it)
            }
        }

        cacheViewModel.responseNotFound.observe(this) {
            if (it)
                if(SystemServiceManagers.isInternetConnected(this)){
                    searchedImageViewModel.getSearchImages(searchQuery)
                }else{
                    searchedImageViewModel.isLoading.value = false
                    setVisibilityAccordingResult(false)
                    DialogUtils.neutralAlertDialog(this, "Connectivity Problem", "Please make sure you have internet connection")
                }
        }

        cacheViewModel.searchResponse.observe(this) {
            setVisibilityAccordingResult(!it.photos.isEmpty())
            setupWallpaperRv(it.photos)
        }

        searchedImageViewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
            binding.wallpaperRv.isVisible = !it
        }
    }

    private fun setVisibilityAccordingResult(isResultFound: Boolean) {
        binding.wallpaperRv.isVisible = isResultFound
        binding.notFoundTv.isVisible = !isResultFound
    }

    private fun setupWallpaperRv(list: List<Photo>) {
        val rvWallpaperAdapter = WallpaperDisplayAdapter(this, list, onImageClick)
        binding.wallpaperRv.adapter = rvWallpaperAdapter
        searchedImageViewModel.isLoading.value = false
    }

}