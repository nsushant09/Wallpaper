package com.neupanesushant.wallpaper.data.repository

import com.neupanesushant.wallpaper.BuildConfig
import com.neupanesushant.wallpaper.domain.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Endpoints {
    @GET("v1/search")
    suspend fun getSearchedPhoto(
        @Header("Authorization") authKey: String = BuildConfig.PEXELS_API_KEY,
        @Query("query") searchLabel: String
    ): SearchResponse

    @GET("v1/search")
    suspend fun getSearchedPhotoWithPerPage(
        @Header("Authorization") authKey: String = BuildConfig.PEXELS_API_KEY,
        @Query("query") searchLabel: String,
        @Query("per_page") per_page : Int
    ): SearchResponse
}