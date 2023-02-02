package com.neupanesushant.wallpaper.components.activities

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.neupanesushant.wallpaper.R
import com.neupanesushant.wallpaper.components.data.AndroidDownloader
import com.neupanesushant.wallpaper.components.data.Downloader
import com.neupanesushant.wallpaper.components.extras.DialogUtils
import com.neupanesushant.wallpaper.components.extras.SystemServiceManagers
import com.neupanesushant.wallpaper.components.viewmodels.WallpaperViewModel
import com.neupanesushant.wallpaper.databinding.ActivityWallpaperViewBinding
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.showText
import kotlinx.coroutines.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            if(SystemServiceManagers.isInternetConnected(this)){
                downloadImage()
            }else{
                DialogUtils.neutralAlertDialog(this, "Connectivity Problem", "Please make sure you have internet connection \n\nRetry your download after connection")
            }
        }

        binding.btnApply.setOnClickListener {
            if (isApplyButtonActive) {
                if(SystemServiceManagers.isInternetConnected(this)){
                    applyWallpaper()
                    isApplyButtonActive = false
                    Handler(Looper.getMainLooper()).postDelayed(
                        { isApplyButtonActive = true }, 10000
                    )
                }else{
                    DialogUtils.neutralAlertDialog(this, "Connectivity Problem", "Please make sure you have internet connection")
                }
            }
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun applyWallpaper() {
        CoroutineScope(newSingleThreadContext("wallpaperApplyThread")).launch {
            suspendApplyWallpaper()
        }
    }

    private suspend fun suspendApplyWallpaper() {
        coroutineScope {
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
                startActivityForResult(intent, WALLPAPER_SET_REQUEST_CODE)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun setupIsFavoriteLayout(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnFavoriteIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_heart_filled
                )
            )
        } else {
            binding.btnFavoriteIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_heart
                )
            )
        }
    }

    private fun getImageUri(fileDir: File, name: String): Uri {
        val newFile = File(fileDir, name)
        return FileProvider.getUriForFile(this, this.packageName + ".provider", newFile)
    }

    private fun showImageInfo(){
        var string = ""

        string += "Photographer\n${wallpaperPhoto.photographer}\n\n"
        string+= "Photographer Link\n${wallpaperPhoto.photographer_url}\n\n"
        string += "Average Color\n${wallpaperPhoto.avg_color}\n\n"
        if(isCurrentPhotoFavorite){
            string += "Favorites\n\n"
        }else{
            string += "Not Favorites\n\n"
        }
        string += "Description\n${wallpaperPhoto.alt}"

        DialogUtils.neutralAlertDialog(this, "Image Info", string)

    }
}