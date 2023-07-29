package com.neupanesushant.wallpaper.view.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.wallpaper.data.datasource.LocalDataSource
import com.neupanesushant.wallpaper.domain.model.Favorites
import com.neupanesushant.wallpaper.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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