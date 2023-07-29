package com.neupanesushant.wallpaper.domain.extras

import com.neupanesushant.wallpaper.domain.model.SearchResponse

object SearchResponseCache {
    val queryResponseMap : MutableMap<String, SearchResponse> = mutableMapOf()
}