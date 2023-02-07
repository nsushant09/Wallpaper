package com.neupanesushant.wallpaper.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = Constants.ROOM_SEARCHRESPONSE_TABLE)
@Parcelize
data class SearchResponsePersistence (
    @PrimaryKey(autoGenerate = false)
    val searchQuery : String,
    val searchResponse : SearchResponse? = null,
    val lastUpdated : Long
        ) : Parcelable