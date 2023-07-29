package com.neupanesushant.wallpaper.data.repository

import com.neupanesushant.wallpaper.domain.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkRepositoryImpl(private val endpoints: Endpoints) : NetworkRepository {
    override fun getSearchedPhotoPerPage(searchLabel: String, perPage: Int) : Flow<SearchResponse> = flow {
        emit(endpoints.getSearchedPhotoWithPerPage(searchLabel = searchLabel, per_page =  perPage))
    }

}