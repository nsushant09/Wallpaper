package com.neupanesushant.wallpaper.data.repository

import com.neupanesushant.wallpaper.domain.model.SearchResponse
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getSearchedPhotoPerPage(searchLabel: String, perPage: Int) : Flow<SearchResponse>
}