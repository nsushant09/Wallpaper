package com.neupanesushant.wallpaper.view.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.wallpaper.data.repository.NetworkRepository
import com.neupanesushant.wallpaper.domain.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchedImageViewModel(
    private val application: Application,
    private val network: NetworkRepository
) : ViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val _searchedImageResponse: MutableLiveData<SearchResponse?> = MutableLiveData()
    val searchedImageResponse: LiveData<SearchResponse?> get() = _searchedImageResponse

    fun getSearchImages(query: String) {
        isLoading.value = true
        viewModelScope.launch {
            network.getSearchedPhotoPerPage(searchLabel = query, perPage = 80)
                .flowOn(Dispatchers.IO)
                .catch { _searchedImageResponse.value = null }
                .collectLatest {
                    _searchedImageResponse.value = it
                }
        }
    }
}