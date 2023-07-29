package com.neupanesushant.wallpaper.view.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.wallpaper.data.datasource.LocalDataSource
import com.neupanesushant.wallpaper.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val application: Application, private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _favoriteImagesList = MutableLiveData<List<Photo>>()
    val favoriteImagesList: LiveData<List<Photo>> get() = _favoriteImagesList

    val isLoading = MutableLiveData<Boolean>()

    fun getFavorites() {
        isLoading.value = true
        viewModelScope.launch {

            localDataSource.getAllFavorites()
                .flowOn(Dispatchers.IO)
                .collectLatest{
                if (it == null) {
                    _favoriteImagesList.postValue(emptyList())
                } else {
                    _favoriteImagesList.postValue(it.mapNotNull {
                        it.photo
                    })
                    isLoading.value = false
                }
            }
        }
    }
}