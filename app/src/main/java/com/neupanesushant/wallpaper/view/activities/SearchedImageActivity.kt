package com.neupanesushant.wallpaper.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.neupanesushant.wallpaper.databinding.ActivitySearchedImageBinding
import com.neupanesushant.wallpaper.domain.extras.DialogUtils
import com.neupanesushant.wallpaper.domain.extras.SystemServiceManagers
import com.neupanesushant.wallpaper.domain.extras.showText
import com.neupanesushant.wallpaper.domain.model.Constants
import com.neupanesushant.wallpaper.domain.model.Photo
import com.neupanesushant.wallpaper.domain.usecase.ad.AdCodes
import com.neupanesushant.wallpaper.domain.usecase.ad.InterstitialAdsManager
import com.neupanesushant.wallpaper.view.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.view.viewmodels.ResponseCacheViewModel
import com.neupanesushant.wallpaper.view.viewmodels.SearchedImageViewModel
import org.koin.android.ext.android.inject
import java.util.Locale

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

    private lateinit var interstitialAdManager: InterstitialAdsManager

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

        interstitialAdManager = InterstitialAdsManager(this, adLoadCallback)
        interstitialAdManager.loadAd(AdCodes.SEARCHED_ACTIVITY_AD_UNIT)

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
                if (SystemServiceManagers.isInternetConnected(this)) {
                    searchedImageViewModel.getSearchImages(searchQuery)
                } else {
                    searchedImageViewModel.isLoading.value = false
                    setVisibilityAccordingResult(false)
                    DialogUtils.neutralAlertDialog(
                        this,
                        "Connectivity Problem",
                        "Please make sure you have internet connection"
                    )
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

    private val adLoadCallback = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            interstitialAdManager.setAd(interstitialAd)
            interstitialAdManager.showAd(this@SearchedImageActivity)
        }
    }
}