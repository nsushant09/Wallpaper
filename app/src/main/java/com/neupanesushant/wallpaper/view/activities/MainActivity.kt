package com.neupanesushant.wallpaper.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.neupanesushant.wallpaper.databinding.ActivityMainBinding
import com.neupanesushant.wallpaper.domain.extras.DialogUtils
import com.neupanesushant.wallpaper.domain.extras.SystemServiceManagers
import com.neupanesushant.wallpaper.domain.extras.hideKeyboard
import com.neupanesushant.wallpaper.domain.model.Constants
import com.neupanesushant.wallpaper.domain.model.KeyValue
import com.neupanesushant.wallpaper.domain.model.Photo
import com.neupanesushant.wallpaper.domain.usecase.ad.BannerAdsManager
import com.neupanesushant.wallpaper.view.adapter.WallpaperDisplayAdapter
import com.neupanesushant.wallpaper.view.fragment.BottomSheetCallback
import com.neupanesushant.wallpaper.view.fragment.CategoryBottomSheet
import com.neupanesushant.wallpaper.view.viewmodels.MainViewModel
import com.neupanesushant.wallpaper.view.viewmodels.ResponseCacheViewModel
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

    private var sliderList = arrayListOf<KeyValue>()

    private lateinit var bannerAdsManager: BannerAdsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bannerAdsManager = BannerAdsManager(this)

        setupViews()
        setupEventListener()
        setupObservers()
    }

    private fun setupViews() {
        binding.adView.adListener = adViewListener
        bannerAdsManager.loadAd(binding.adView)
        cacheViewModel.getSearchResponse("Random")

        binding.wallpaperRv.layoutManager = GridLayoutManager(this, 2)

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

        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchEt.clearFocus()
                binding.searchEt.hideKeyboard()
                callSearchedImageActivity(binding.searchEt.text.toString())
            }

            true
        }
    }

    private val adViewListener = object : AdListener() {
        override fun onAdFailedToLoad(error: LoadAdError) {
            Log.i("MainActivityError", error.message.toString())
            bannerAdsManager.loadAd(binding.adView)
            binding.adView.isVisible = false

        }

        override fun onAdLoaded() {
            binding.adView.isVisible = true
        }
    }

    private fun setupObservers() {
        mainViewModel.imageResponse.observe(this) {
            setupWallpaperRv(it.photos)
            cacheViewModel.addSearchResponse("Random", it)
        }

        cacheViewModel.responseNotFound.observe(this) {
            if (it)
                if (SystemServiceManagers.isInternetConnected(this)) {
                    mainViewModel.getRandomImages()
                } else {
                    mainViewModel.isLoading.value = false
                    DialogUtils.neutralAlertDialog(
                        this,
                        "Connectivity Problem",
                        "Please make sure you have internet connection"
                    )
                }
        }

        cacheViewModel.searchResponse.observe(this) {
            setupWallpaperRv(it.photos)
        }

        mainViewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
            binding.contentVisibleContainer.isVisible = !it
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

    init {
        setSliderList()
    }

    private fun setSliderList() {
        sliderList.apply {
            clear()
            add(
                KeyValue(
                    "Animals",
                    "https://images.pexels.com/photos/927497/pexels-photo-927497.jpeg?auto=compress&cs=tinysrgb&w=600"
                )
            )
            add(
                KeyValue(
                    "Art",
                    "https://images.pexels.com/photos/1621793/pexels-photo-1621793.jpeg?auto=compress&cs=tinysrgb&w=600"
                )
            )
            add(
                KeyValue(
                    "Fashion",
                    "https://images.pexels.com/photos/1884584/pexels-photo-1884584.jpeg?auto=compress&cs=tinysrgb&w=600"
                )
            )
            add(
                KeyValue(
                    "Foggy Forests",
                    "https://images.pexels.com/photos/9754/mountains-clouds-forest-fog.jpg?auto=compress&cs=tinysrgb&w=600"
                )
            )
            add(
                KeyValue(
                    "Lifestyle",
                    "https://images.pexels.com/photos/237272/pexels-photo-237272.jpeg?auto=compress&cs=tinysrgb&w=600"
                )
            )
            add(
                KeyValue(
                    "Minimalism",
                    "https://images.pexels.com/photos/262367/pexels-photo-262367.jpeg?auto=compress&cs=tinysrgb&w=600"
                )
            )
            add(
                KeyValue(
                    "Plants",
                    "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?auto=compress&cs=tinysrgb&w=600"
                )
            )
            add(
                KeyValue(
                    "Space",
                    "https://images.pexels.com/photos/1938348/pexels-photo-1938348.jpeg?auto=compress&cs=tinysrgb&w=600"
                )
            )
        }
    }

}