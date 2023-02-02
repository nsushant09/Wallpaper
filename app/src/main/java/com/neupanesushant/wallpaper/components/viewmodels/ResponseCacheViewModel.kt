package com.neupanesushant.wallpaper.components.viewmodels

import android.app.Application
import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.wallpaper.components.data.NetworkRepository
import com.neupanesushant.wallpaper.components.extras.SearchResponseCache
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.SearchResponse
import com.neupanesushant.wallpaper.model.SearchResponsePersistence
import com.neupanesushant.wallpaper.persistence.FavoritesDAO
import com.neupanesushant.wallpaper.persistence.SearchResponseDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResponseCacheViewModel(
    private val application: Application,
    private val favoritesDAO: FavoritesDAO,
    private val searchResponseDAO: SearchResponseDAO,
    private val network: NetworkRepository
) : ViewModel() {

    private val _searchResponse = MutableLiveData<SearchResponse>()
    val searchResponse: LiveData<SearchResponse> get() = _searchResponse

    private val _responseNotFound = MutableLiveData<Boolean>()
    val responseNotFound: LiveData<Boolean> get() = _responseNotFound

    fun addSearchResponse(searchQuery: String, searchResponse: SearchResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            searchResponseDAO.insertSearchResponse(
                SearchResponsePersistence(
                    searchQuery,
                    searchResponse
                )
            )
        }.invokeOnCompletion {
            if (it == null) {
                SearchResponseCache.queryResponseMap.put(searchQuery, searchResponse)
            }
        }
    }

    fun removeSearchResponse(deleteQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchResponseDAO.deleteSearchResponse(deleteQuery)
        }.invokeOnCompletion {
            if (it == null) {
                SearchResponseCache.queryResponseMap.remove(deleteQuery)
            }
        }
    }

    fun updateSearchResponse(searchQuery: String, newSearchResponse: SearchResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            searchResponseDAO.updateSearchResponse(
                SearchResponsePersistence(
                    searchQuery,
                    newSearchResponse
                )
            )
        }.invokeOnCompletion {
            if (it == null) {
                if (SearchResponseCache.queryResponseMap.contains(searchQuery)) {
                    SearchResponseCache.queryResponseMap.replace(searchQuery, newSearchResponse)
                }

                getSearchResponse(searchQuery)
            }
        }
    }

    fun getSearchResponse(searchQuery: String) {

        if (SearchResponseCache.queryResponseMap.contains(searchQuery)) {
            _searchResponse.postValue(SearchResponseCache.queryResponseMap.get(searchQuery))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = searchResponseDAO.getRequireSearchResponse(
                    SimpleSQLiteQuery("SELECT * FROM " + Constants.ROOM_SEARCHRESPONSE_TABLE + " WHERE searchQuery = '" + searchQuery + "' LIMIT 1")
                )
                if (response == null) {
                    _responseNotFound.postValue(true)
                } else {
                    response.searchResponse?.let {
                        _searchResponse.postValue(it)
                    }
                    _responseNotFound.postValue(false)
                }
            } catch (e: SQLiteException) {
                e.printStackTrace()
                _responseNotFound.postValue(true)
            }
        }
    }

}