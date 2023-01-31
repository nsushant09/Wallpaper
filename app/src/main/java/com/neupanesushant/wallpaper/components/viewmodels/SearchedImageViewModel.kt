package com.neupanesushant.wallpaper.components.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.wallpaper.Endpoints
import com.neupanesushant.wallpaper.model.SearchResponse
import kotlinx.coroutines.launch

class SearchedImageViewModel(private val application : Application, private val endpoints: Endpoints) : ViewModel() {

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private val _searchedImageResponse : MutableLiveData<SearchResponse?> = MutableLiveData()
    val searchedImageResponse : LiveData<SearchResponse?> get() = _searchedImageResponse

    fun getSearchImages(query : String){
        _isLoading.value = true
        viewModelScope.launch{
            kotlin.runCatching {
                _searchedImageResponse.value = endpoints.getSearchedPhotoWithPerPage(searchLabel = query, per_page = 80)
            }.onFailure {
                _searchedImageResponse.value = null
            }
        }.invokeOnCompletion {
            _isLoading.value = false
        }
    }
}