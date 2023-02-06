package com.neupanesushant.wallpaper.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Favorites
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: Favorites)

    @Update
    suspend fun update(favorites: Favorites)

    @Delete
    suspend fun delete(favorites: Favorites)

    @Query("SELECT * FROM " +Constants.ROOM_FAVORITES_TABLE)
    fun getAllFavorites() : Flow<List<Favorites>?>

    @RawQuery
    fun getSelectedFavorite(query : SupportSQLiteQuery) : Favorites?

}