package com.neupanesushant.wallpaper.components.extras

import com.neupanesushant.wallpaper.model.SearchResponse

object SearchResponseCache {
    val queryResponseMap : MutableMap<String, SearchResponse> = mutableMapOf()
}