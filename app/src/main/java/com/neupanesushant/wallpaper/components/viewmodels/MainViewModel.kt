package com.neupanesushant.wallpaper.components.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.wallpaper.Endpoints
import com.neupanesushant.wallpaper.model.SearchResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application, private val endpoints: Endpoints) :
    ViewModel() {

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private val _imageResponse = MutableLiveData<SearchResponse>()
    val imageResponse get() : LiveData<SearchResponse> = _imageResponse

    fun getRandomImages() {
        _isLoading.value = true
        viewModelScope.launch {
            _imageResponse.value = endpoints.getSearchedPhotoWithPerPage(searchLabel = "Random", per_page = 100)
        }.invokeOnCompletion {
            _isLoading.value = false
        }
    }

}