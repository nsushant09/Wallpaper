package com.neupanesushant.wallpaper.components.data

import com.neupanesushant.wallpaper.model.SearchResponse
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getSearchedPhotoPerPage(searchLabel: String, perPage: Int) : Flow<SearchResponse>
}