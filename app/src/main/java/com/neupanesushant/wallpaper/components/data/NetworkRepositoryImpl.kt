package com.neupanesushant.wallpaper.components.data

import com.neupanesushant.wallpaper.Endpoints
import com.neupanesushant.wallpaper.model.SearchResponse

class NetworkRepositoryImpl(private val endpoints: Endpoints) : NetworkRepository {
    override suspend fun getSearchedPhotoPerPage(searchLabel: String, perPage: Int) : SearchResponse{
        return endpoints.getSearchedPhotoWithPerPage(searchLabel = searchLabel, per_page = perPage)
    }

}