package com.neupanesushant.wallpaper.components.data

import androidx.room.util.query
import com.neupanesushant.wallpaper.Endpoints
import com.neupanesushant.wallpaper.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkRepositoryImpl(private val endpoints: Endpoints) : NetworkRepository {
    override fun getSearchedPhotoPerPage(searchLabel: String, perPage: Int) : Flow<SearchResponse> = flow {
        emit(endpoints.getSearchedPhotoWithPerPage(searchLabel = searchLabel, per_page =  perPage))
    }

}