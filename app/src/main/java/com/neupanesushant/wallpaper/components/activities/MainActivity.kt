package com.neupanesushant.wallpaper.components.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.neupanesushant.wallpaper.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.components.extras.BottomSheetCallback
import com.neupanesushant.wallpaper.components.extras.DialogUtils
import com.neupanesushant.wallpaper.components.extras.SystemServiceManagers
import com.neupanesushant.wallpaper.components.fragment.CategoryBottomSheet
import com.neupanesushant.wallpaper.components.viewmodels.MainViewModel
import com.neupanesushant.wallpaper.components.viewmodels.ResponseCacheViewModel
import com.neupanesushant.wallpaper.databinding.ActivityMainBinding
import com.neupanesushant.wallpaper.hideKeyboard
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.showText
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by inject()
    private val cacheViewModel: ResponseCacheViewModel by inject()
    private val onImageClick: (Photo) -> Unit = { photo ->
        val intent = Intent(this, WallpaperViewActivity::class.java)
        intent.putExtra(Constants.WALLPAPER_PHOTO, photo)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupEventListener()
        setupObservers()
    }

    private fun setupViews() {
        binding.wallpaperRv.layoutManager = GridLayoutManager(this, 2)
        binding.searchEt.visibility = View.GONE
        cacheViewModel.getSearchResponse("Random")
    }

    private fun setupEventListener() {
        binding.btnCategory.setOnClickListener {
            val categoryBottomSheet = CategoryBottomSheet.getInstance(object : BottomSheetCallback {
                override fun onClick(string: String) {
                    callSearchedImageActivity(string)
                }
            })
            categoryBottomSheet.show(supportFragmentManager, categoryBottomSheet::class.java.name)
        }

        binding.btnFavorites.setOnClickListener {
            Intent(this, FavoritesActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.btnSearch.setOnClickListener {
            val transition = Fade()
            transition.duration = 300
            transition.addTarget(binding.searchEt)
            TransitionManager.beginDelayedTransition(binding.root, transition)
            binding.searchEt.isVisible = !binding.searchEt.isVisible
        }

        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchEt.clearFocus()
                binding.searchEt.hideKeyboard()
                callSearchedImageActivity(binding.searchEt.text.toString())
            }

            true
        }
    }

    private fun setupObservers() {
        mainViewModel.imageResponse.observe(this) {
            setupWallpaperRv(it.photos)
            cacheViewModel.addSearchResponse("Random", it)
        }

        cacheViewModel.responseNotFound.observe(this){
            if(it)
                if(SystemServiceManagers.isInternetConnected(this)){
                    mainViewModel.getRandomImages()
                }else{
                    mainViewModel.isLoading.value = false
                    DialogUtils.neutralAlertDialog(this, "Connectivity Problem", "Please make sure you have internet connection")
                }
        }

        cacheViewModel.searchResponse.observe(this){
            setupWallpaperRv(it.photos)
        }

        mainViewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
            binding.wallpaperRv.isVisible = !it
        }
    }

    private fun setupWallpaperRv(list: List<Photo>) {
        val rvWallpaperAdapter = WallpaperDisplayAdapter(this, list, onImageClick)
        binding.wallpaperRv.adapter = rvWallpaperAdapter
        mainViewModel.isLoading.value = false
    }

    private fun callSearchedImageActivity(searchQuery: String) {
        val intent = Intent(this, SearchedImageActivity::class.java)
        intent.putExtra(Constants.SEARCH_QUERY, searchQuery)
        startActivity(intent)
    }
}