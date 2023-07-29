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

class MainViewModel(private val application: Application, private val network : NetworkRepository) :
    ViewModel() {

   val isLoading : MutableLiveData<Boolean> = MutableLiveData()

    private val _imageResponse = MutableLiveData<SearchResponse>()
    val imageResponse get() : LiveData<SearchResponse> = _imageResponse

    fun getRandomImages() {
        isLoading.value = true
        viewModelScope.launch{
            network.getSearchedPhotoPerPage(searchLabel = "Random", perPage = 100)
                .flowOn(Dispatchers.IO)
                .catch{}
                .collectLatest {
                    _imageResponse.value = it
                }
        }
    }

}