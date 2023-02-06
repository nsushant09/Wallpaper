package com.neupanesushant.wallpaper.components.data

import androidx.sqlite.db.SimpleSQLiteQuery
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Favorites
import com.neupanesushant.wallpaper.model.SearchResponsePersistence
import com.neupanesushant.wallpaper.persistence.FavoritesDAO
import com.neupanesushant.wallpaper.persistence.SearchResponseDAO
import kotlinx.coroutines.flow.Flow

class LocalRepositoryImpl(private val favoritesDAO: FavoritesDAO, private val searchResponseDAO: SearchResponseDAO) : LocalDataSource {
    override suspend fun insertFavorites(favorites: Favorites) {
        favoritesDAO.insertFavorites(favorites)
    }

    override suspend fun updateFavorites(favorites: Favorites) {
        favoritesDAO.update(favorites)
    }

    override suspend fun deleteFavorites(favorites: Favorites) {
        favoritesDAO.delete(favorites)
    }

    override fun getAllFavorites(): Flow<List<Favorites>?> {
        return favoritesDAO.getAllFavorites()
    }

    override suspend fun getSelectedFavorites(searchQuery: Int): Favorites? {
        return favoritesDAO.getSelectedFavorite(
            SimpleSQLiteQuery("SELECT * FROM " + Constants.ROOM_FAVORITES_TABLE + " WHERE id = " + searchQuery + " LIMIT 1")
        )
    }

    override suspend fun insertSearchResponse(searchResponsePersistence: SearchResponsePersistence) {
        searchResponseDAO.insertSearchResponse(searchResponsePersistence)
    }

    override suspend fun updateSearchResponse(searchResponsePersistence: SearchResponsePersistence) {
        searchResponseDAO.updateSearchResponse(searchResponsePersistence)
    }

    override suspend fun deleteSearchResponse(deleteQuery: String) {
        searchResponseDAO.deleteSearchResponse(deleteQuery)
    }

    override fun getAllSearchResponse(): Flow<List<SearchResponsePersistence>?> {
        return searchResponseDAO.getAllSearchResponse()
    }

    override suspend fun getRequireSearchResponse(searchQuery: String): SearchResponsePersistence? {
        return searchResponseDAO.getRequireSearchResponse(
            SimpleSQLiteQuery("SELECT * FROM " + Constants.ROOM_SEARCHRESPONSE_TABLE + " WHERE searchQuery = '" + searchQuery + "' LIMIT 1")
        )
    }
}