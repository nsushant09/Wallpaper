package com.neupanesushant.wallpaper.components.data

import com.neupanesushant.wallpaper.model.Favorites
import com.neupanesushant.wallpaper.model.SearchResponsePersistence

interface LocalDataSource {

    suspend fun insertFavorites(favorites : Favorites)
    suspend fun updateFavorites(favorites : Favorites)
    suspend fun deleteFavorites(favorites : Favorites)
    suspend fun getAllFavorites() : List<Favorites>?
    suspend fun getSelectedFavorites(searchQuery : Int) : Favorites?

    suspend fun insertSearchResponse(searchResponsePersistence: SearchResponsePersistence)
    suspend fun updateSearchResponse(searchResponsePersistence: SearchResponsePersistence)
    suspend fun deleteSearchResponse(deleteQuery : String)
    suspend fun getAllSearchResponse() : List<SearchResponsePersistence>?
    suspend fun getRequireSearchResponse(searchQuery : String) : SearchResponsePersistence?

}