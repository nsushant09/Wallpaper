package com.neupanesushant.wallpaper.components

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.neupanesushant.wallpaper.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.components.viewmodels.MainViewModel
import com.neupanesushant.wallpaper.databinding.ActivityMainBinding
import com.neupanesushant.wallpaper.hideKeyboard
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.showText
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by inject()
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
        mainViewModel.getRandomImages()
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

        binding.btnCollection.setOnClickListener {
            this.showText("This feature will be added soon!!!")
        }

        binding.btnSearch.setOnClickListener {
            val transition = Fade()
            transition.duration = 300
            transition.addTarget(binding.searchEt)
            TransitionManager.beginDelayedTransition(binding.root, transition)
            binding.searchEt.isVisible = !binding.searchEt.isVisible
        }

        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->

            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                binding.searchEt.clearFocus()
                binding.searchEt.hideKeyboard()
                callSearchedImageActivity(binding.searchEt.text.toString())
            }

            true
        }
    }

    private fun setupObservers() {
        mainViewModel.imageResponse.observe(this) {
            val rvWallpaperAdapter = WallpaperDisplayAdapter(this, it.photos, onImageClick)
            binding.wallpaperRv.adapter = rvWallpaperAdapter
        }

        mainViewModel.isLoading.observe(this){
            binding.progressBar.isVisible = it
            binding.wallpaperRv.isVisible = !it
        }
    }

    private fun callSearchedImageActivity(searchQuery: String) {
        val intent = Intent(this, SearchedImageActivity::class.java)
        intent.putExtra(Constants.SEARCH_QUERY, searchQuery)
        startActivity(intent)
    }
}