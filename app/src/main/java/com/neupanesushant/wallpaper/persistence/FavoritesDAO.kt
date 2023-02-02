package com.neupanesushant.wallpaper.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Favorites

@Dao
interface FavoritesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: Favorites)

    @Update
    suspend fun update(favorites: Favorites)

    @Delete
    suspend fun delete(favorites: Favorites)

    @Query("SELECT * FROM " +Constants.ROOM_FAVORITES_TABLE)
    suspend fun getAllFavorites() : List<Favorites>?

    @RawQuery
    fun getSelectedFavorite(query : SupportSQLiteQuery) : Favorites?

}