package com.neupanesushant.wallpaper.components.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.wallpaper.Endpoints
import com.neupanesushant.wallpaper.components.data.NetworkRepository
import com.neupanesushant.wallpaper.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchedImageViewModel(private val application : Application, private val network: NetworkRepository) : ViewModel() {

    val isLoading : MutableLiveData<Boolean> = MutableLiveData()

    private val _searchedImageResponse : MutableLiveData<SearchResponse?> = MutableLiveData()
    val searchedImageResponse : LiveData<SearchResponse?> get() = _searchedImageResponse

    fun getSearchImages(query : String){
        isLoading.value = true
        viewModelScope.launch{
            kotlin.runCatching {
                _searchedImageResponse.value = network.getSearchedPhotoPerPage(searchLabel = query, perPage = 80)
            }.onFailure {
                _searchedImageResponse.value = null
            }
        }.invokeOnCompletion {
            isLoading.value = false
        }
    }
}