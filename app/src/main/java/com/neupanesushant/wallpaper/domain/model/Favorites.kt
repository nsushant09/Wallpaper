package com.neupanesushant.wallpaper.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = Constants.ROOM_FAVORITES_TABLE)
@Parcelize
data class Favorites (
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val photo : Photo? = null,
        ) : Parcelable

