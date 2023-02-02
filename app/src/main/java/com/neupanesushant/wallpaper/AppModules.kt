package com.neupanesushant.wallpaper

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.neupanesushant.wallpaper.components.data.NetworkRepository
import com.neupanesushant.wallpaper.components.data.NetworkRepositoryImpl
import com.neupanesushant.wallpaper.components.viewmodels.*
import com.neupanesushant.wallpaper.model.Constants
import com.neupanesushant.wallpaper.model.Favorites
import com.neupanesushant.wallpaper.model.SearchResponsePersistence
import com.neupanesushant.wallpaper.persistence.FavoritesDAO
import com.neupanesushant.wallpaper.persistence.SearchResponseDAO
import com.neupanesushant.wallpaper.persistence.WallpaperDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL_PEXELS = "https://api.pexels.com/"
const val PEXELS_API_KEY = "563492ad6f9170000100000143117b6d868a4e458df8fc34a264a629"
fun appModules() = module {

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_PEXELS)
            .build()
            .create(Endpoints::class.java)

    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    single<WallpaperDatabase>{
        Room.databaseBuilder(androidApplication(), WallpaperDatabase::class.java, Constants.ROOM_DATABASE_NAME).build()
    }

    single<FavoritesDAO>{
        get<WallpaperDatabase>().favoritesDao()
    }

    single<SearchResponseDAO>{
        get<WallpaperDatabase>().searchResponseDao()
    }

    single<NetworkRepository> {
        NetworkRepositoryImpl(get())
    }

    viewModel {
        MainViewModel(androidApplication(), get())
    }

    viewModel {
        SearchedImageViewModel(androidApplication(), get())
    }

    viewModel {
        WallpaperViewModel(androidApplication(), get())
    }

    viewModel{
        FavoritesViewModel(androidApplication(), get())
    }

    viewModel{
        ResponseCacheViewModel(androidApplication(), get(), get(), get())
    }

}