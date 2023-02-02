package com.neupanesushant.wallpaper.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Favorites
import com.neupanesushant.wallpaper.model.SearchResponse
import com.neupanesushant.wallpaper.model.SearchResponsePersistence

@Dao
interface SearchResponseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResponse(searchResponsePersistence: SearchResponsePersistence)

    @Update
    suspend fun updateSearchResponse(searchResponsePersistence: SearchResponsePersistence)

    @Query("DELETE FROM " + Constants.ROOM_SEARCHRESPONSE_TABLE+ " WHERE searchQuery = :deleteQuery")
    suspend fun deleteSearchResponse(deleteQuery : String)

    @Query("SELECT * FROM " + Constants.ROOM_SEARCHRESPONSE_TABLE)
    suspend fun getAllSearchResponse() : List<SearchResponsePersistence>

    @RawQuery
    fun getRequireSearchResponse(query : SupportSQLiteQuery) : SearchResponsePersistence?
}
