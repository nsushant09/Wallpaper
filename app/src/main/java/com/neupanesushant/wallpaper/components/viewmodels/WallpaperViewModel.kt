package com.neupanesushant.wallpaper.components.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.neupanesushant.wallpaper.WallpaperApplication
import com.neupanesushant.wallpaper.components.data.LocalDataSource
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Favorites
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.persistence.FavoritesDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WallpaperViewModel(
    private val application: Application,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _isCurrentWallpaperFavorite = MutableLiveData<Boolean>()
    val isCurrentWallpaperFavorite: LiveData<Boolean> get() = _isCurrentWallpaperFavorite

    private val _favoriteImageCache = MutableLiveData<Favorites?>()

    fun checkCurrentWallpaperFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentValueAsFavorite = localDataSource.getSelectedFavorites(id)
            if (currentValueAsFavorite != null) {
                _favoriteImageCache.postValue(currentValueAsFavorite)
            }
            _isCurrentWallpaperFavorite.postValue(currentValueAsFavorite != null)
        }
    }

    fun addToFavorites(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            localDataSource.insertFavorites(Favorites(photo.id, photo))
        }.invokeOnCompletion {
            checkCurrentWallpaperFavorite(photo.id)
        }
    }

    fun deleteFromFavorites() {
        _favoriteImageCache.value?.let { photo ->
            viewModelScope.launch(Dispatchers.IO) {
                localDataSource.deleteFavorites(photo)
            }.invokeOnCompletion {
                checkCurrentWallpaperFavorite(photo.id)
            }
        }
    }

}