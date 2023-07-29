package com.neupanesushant.wallpaper.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.neupanesushant.wallpaper.domain.model.Favorites
import com.neupanesushant.wallpaper.domain.model.SearchResponsePersistence
import com.neupanesushant.wallpaper.domain.usecase.RoomConvertors

@Database(entities = [Favorites::class, SearchResponsePersistence::class], version = 1, exportSchema = false)
@TypeConverters(RoomConvertors::class)
abstract class WallpaperDatabase : RoomDatabase(){
    abstract fun favoritesDao() : FavoritesDAO
    abstract fun searchResponseDao() : SearchResponseDAO
}