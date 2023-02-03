package com.neupanesushant.wallpaper.components.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.wallpaper.components.data.LocalDataSource
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.persistence.FavoritesDAO
import kotlinx.coroutines.launch

class FavoritesViewModel(private val application : Application, private val localDataSource: LocalDataSource) : ViewModel() {

    private val _favoriteImagesList = MutableLiveData<List<Photo>>()
    val favoriteImagesList : LiveData<List<Photo>> get() = _favoriteImagesList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    fun getFavorites(){
        _isLoading.value = true
        viewModelScope.launch{
            val response = localDataSource.getAllFavorites()

            if(response != null){
                val photoList = response.mapNotNull {
                    it.photo
                }
                _favoriteImagesList.postValue(photoList)
            }else{
                _favoriteImagesList.postValue(emptyList())
            }

        }.invokeOnCompletion {
            _isLoading.value = false
        }
    }
}