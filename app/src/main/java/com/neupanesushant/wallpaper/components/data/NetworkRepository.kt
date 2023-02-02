package com.neupanesushant.wallpaper.components.data

import com.neupanesushant.wallpaper.model.SearchResponse

interface NetworkRepository {
    suspend fun getSearchedPhotoPerPage(searchLabel: String, perPage: Int) : SearchResponse
}