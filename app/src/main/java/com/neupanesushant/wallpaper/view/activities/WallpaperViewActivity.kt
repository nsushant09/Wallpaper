package com.neupanesushant.wallpaper.view.activities

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.neupanesushant.wallpaper.R
import com.neupanesushant.wallpaper.databinding.ActivityWallpaperViewBinding
import com.neupanesushant.wallpaper.domain.extras.DialogUtils
import com.neupanesushant.wallpaper.domain.extras.SystemServiceManagers
import com.neupanesushant.wallpaper.domain.extras.showText
import com.neupanesushant.wallpaper.domain.model.Constants
import com.neupanesushant.wallpaper.domain.model.Photo
import com.neupanesushant.wallpaper.domain.usecase.AndroidDownloader
import com.neupanesushant.wallpaper.domain.usecase.Downloader
import com.neupanesushant.wallpaper.domain.usecase.ad.InterstitialAdsManager
import com.neupanesushant.wallpaper.view.viewmodels.WallpaperViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class WallpaperViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWallpaperViewBinding
    private lateinit var wallpaperPhoto: Photo
    private lateinit var downloader: Downloader
    private val WALLPAPER_SET_REQUEST_CODE = 12896734;
    private var isApplyButtonActive = true
    private val wallpaperViewModel: WallpaperViewModel by inject()
    private var isCurrentPhotoFavorite: Boolean = false

    private lateinit var interstitialAdsManager: InterstitialAdsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        interstitialAdsManager =
            InterstitialAdsManager(this, addLoadCallback)
        interstitialAdsManager.loadAd()

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
        wallpaperViewModel.checkCurrentWallpaperFavorite(wallpaperPhoto.id)
    }

    private fun setupEventListener() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnFavorite.setOnClickListener {
            if (isCurrentPhotoFavorite) {
                wallpaperViewModel.deleteFromFavorites()
            } else {
                wallpaperViewModel.addToFavorites(wallpaperPhoto)
            }
        }

        binding.btnInfo.setOnClickListener {
            showImageInfo()
        }

        binding.btnDownload.setOnClickListener {

            if (!SystemServiceManagers.isInternetConnected(this)) {
                DialogUtils.neutralAlertDialog(
                    this,
                    "Connectivity Problem",
                    "Please make sure you have internet connection \n\nRetry your download after connection"
                )
                return@setOnClickListener
            }

            interstitialAdsManager.setContentCallback(getContentCallBack {
                interstitialAdsManager.loadAd()
                downloadImage()
            })
            interstitialAdsManager.showAd(this)
        }

        binding.btnApply.setOnClickListener {

            if (!isApplyButtonActive)
                return@setOnClickListener

            if (!SystemServiceManagers.isInternetConnected(this)) {
                DialogUtils.neutralAlertDialog(
                    this,
                    "Connectivity Problem",
                    "Please make sure you have internet connection"
                )
                return@setOnClickListener
            }

            var deferredIntent: Deferred<Intent?>? = null
            CoroutineScope(Dispatchers.Default).launch {
                deferredIntent = async {
                    getWallpaperIntent()
                }
            }

            interstitialAdsManager.setContentCallback(getContentCallBack {
                interstitialAdsManager.loadAd()

                if (deferredIntent == null)
                    return@getContentCallBack

                CoroutineScope(Dispatchers.Default).launch {
                    val intent = deferredIntent!!.await() ?: return@launch
                    startActivityForResult(intent, WALLPAPER_SET_REQUEST_CODE)
                    isApplyButtonActive = false
                    Handler(Looper.getMainLooper()).postDelayed(
                        { isApplyButtonActive = true }, 10000
                    )
                }
            })

            interstitialAdsManager.showAd(this)
        }
    }

    private fun setupObserver() {
        wallpaperViewModel.isCurrentWallpaperFavorite.observe(this) {
            isCurrentPhotoFavorite = it
            setupIsFavoriteLayout(it)
        }
    }

    private fun downloadImage() {
        downloader = AndroidDownloader(this)
        downloader.downloadFile(wallpaperPhoto.src.original, AndroidDownloader.IMAGE_JPEG)
    }

    private suspend fun getWallpaperIntent(): Intent? {
        return withContext(Dispatchers.IO) {
            val wallpaperManager = WallpaperManager.getInstance(this@WallpaperViewActivity)
            val url = URL(wallpaperPhoto.src.original)
            val filename = System.currentTimeMillis().toString() + ".jpeg"
            val bitmap = BitmapFactory.decodeStream(withContext(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    url.openConnection()
                }.getInputStream()
            })

            try {
                val cachePath = File(applicationContext.cacheDir, "images")
                cachePath.mkdir()
                withContext(Dispatchers.IO) {
                    val stream = FileOutputStream("$cachePath/$filename")
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.close()
                }
                val intent =
                    wallpaperManager.getCropAndSetWallpaperIntent(getImageUri(cachePath, filename))
                intent
            } catch (e: java.lang.Exception) {
                null
            }

        }
    }

    private fun setupIsFavoriteLayout(isFavorite: Boolean) {
        val icon: Int = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart
        binding.btnFavoriteIcon.setImageDrawable(ContextCompat.getDrawable(this, icon))
    }

    private fun getImageUri(fileDir: File, name: String): Uri {
        val newFile = File(fileDir, name)
        return FileProvider.getUriForFile(this, this.packageName + ".provider", newFile)
    }

    private fun showImageInfo() {
        var string = ""

        string += "Photographer\n${wallpaperPhoto.photographer}\n\n"
        string += "Photographer Link\n${wallpaperPhoto.photographer_url}\n\n"
        string += "Average Color\n${wallpaperPhoto.avg_color}\n\n"
        if (isCurrentPhotoFavorite) {
            string += "Favorites\n\n"
        } else {
            string += "Not Favorites\n\n"
        }
        string += "Description\n${wallpaperPhoto.alt}"

        DialogUtils.neutralAlertDialog(this, "Image Info", string)

    }

    private fun getContentCallBack(callback: () -> Unit): FullScreenContentCallback {
        return object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                callback()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                callback()
            }
        }
    }


    private val addLoadCallback = object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            interstitialAdsManager.setAd<InterstitialAd>(interstitialAd)
        }
    }
}