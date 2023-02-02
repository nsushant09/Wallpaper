package com.neupanesushant.wallpaper.components.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.neupanesushant.wallpaper.WallpaperApplication
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Favorites
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.persistence.FavoritesDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WallpaperViewModel(
    private val application: Application,
    private val favoritesDAO: FavoritesDAO
) : ViewModel() {

    private val _isCurrentWallpaperFavorite = MutableLiveData<Boolean>()
    val isCurrentWallpaperFavorite: LiveData<Boolean> get() = _isCurrentWallpaperFavorite

    private val _favoriteImageCache = MutableLiveData<Favorites?>()

    fun checkCurrentWallpaperFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentValueAsFavorite = favoritesDAO.getSelectedFavorite(
                SimpleSQLiteQuery("SELECT * FROM " + Constants.ROOM_FAVORITES_TABLE + " WHERE id = " + id + " LIMIT 1")
            )
            if (currentValueAsFavorite != null) {
                _favoriteImageCache.postValue(currentValueAsFavorite)
            }
            _isCurrentWallpaperFavorite.postValue(currentValueAsFavorite != null)
        }
    }

    fun addToFavorites(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesDAO.insertFavorites(Favorites(photo.id, photo))
        }.invokeOnCompletion {
            checkCurrentWallpaperFavorite(photo.id)
        }
    }

    fun deleteFromFavorites() {
        _favoriteImageCache.value?.let { photo ->
            viewModelScope.launch(Dispatchers.IO) {
                favoritesDAO.delete(photo)
            }.invokeOnCompletion {
                checkCurrentWallpaperFavorite(photo.id)
            }
        }
    }

}