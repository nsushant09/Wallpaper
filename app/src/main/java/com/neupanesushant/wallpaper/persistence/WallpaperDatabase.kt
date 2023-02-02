package com.neupanesushant.wallpaper.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.neupanesushant.wallpaper.model.Favorites
import com.neupanesushant.wallpaper.model.SearchResponsePersistence

@Database(entities = [Favorites::class, SearchResponsePersistence::class], version = 1, exportSchema = false)
@TypeConverters(RoomConvertors::class)
abstract class WallpaperDatabase : RoomDatabase(){
    abstract fun favoritesDao() : FavoritesDAO
    abstract fun searchResponseDao() : SearchResponseDAO
}