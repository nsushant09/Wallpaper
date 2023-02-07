package com.neupanesushant.wallpaper.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neupanesushant.wallpaper.model.Photo
import com.neupanesushant.wallpaper.model.SearchResponse
import java.util.*

class RoomConvertors {
    @TypeConverter
    fun savePhoto(photo : Photo) : String?{
        return Gson().toJson(photo)
    }

    @TypeConverter
    fun getPhoto(photoString : String?) : Photo?{
        return Gson().fromJson(
            photoString,
            object : TypeToken<Photo?>() {}.type
        )
    }

    @TypeConverter
    fun saveSearchResponse(searchResponse: SearchResponse) : String?{
        return Gson().toJson(searchResponse)
    }

    @TypeConverter
    fun getSearchResponse(searchResponseString : String) : SearchResponse?{
        return Gson().fromJson(
            searchResponseString,
            object : TypeToken<SearchResponse?>() {}.type
        )
    }

}