package com.neupanesushant.wallpaper.data.datasource

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.wallpaper.domain.model.Constants
import com.neupanesushant.wallpaper.domain.model.Favorites
import com.neupanesushant.wallpaper.domain.model.SearchResponse
import com.neupanesushant.wallpaper.domain.model.SearchResponsePersistence
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchResponseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResponse(searchResponsePersistence: SearchResponsePersistence)

    @Update
    suspend fun updateSearchResponse(searchResponsePersistence: SearchResponsePersistence)

    @Query("DELETE FROM " + Constants.ROOM_SEARCHRESPONSE_TABLE+ " WHERE searchQuery = :deleteQuery")
    suspend fun deleteSearchResponse(deleteQuery : String)

    @Query("SELECT * FROM " + Constants.ROOM_SEARCHRESPONSE_TABLE)
    fun getAllSearchResponse() : Flow<List<SearchResponsePersistence>?>

    @RawQuery
    suspend fun getRequireSearchResponse(query : SupportSQLiteQuery) : SearchResponsePersistence?
}
