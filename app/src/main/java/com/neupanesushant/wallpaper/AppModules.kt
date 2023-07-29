package com.neupanesushant.wallpaper

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.neupanesushant.wallpaper.data.datasource.LocalDataSource
import com.neupanesushant.wallpaper.data.repository.LocalRepositoryImpl
import com.neupanesushant.wallpaper.data.repository.NetworkRepository
import com.neupanesushant.wallpaper.data.repository.NetworkRepositoryImpl
import com.neupanesushant.wallpaper.components.viewmodels.*
import com.neupanesushant.wallpaper.data.repository.Endpoints
import com.neupanesushant.wallpaper.domain.model.Constants
import com.neupanesushant.wallpaper.data.datasource.FavoritesDAO
import com.neupanesushant.wallpaper.data.datasource.SearchResponseDAO
import com.neupanesushant.wallpaper.data.datasource.WallpaperDatabase
import com.neupanesushant.wallpaper.view.viewmodels.FavoritesViewModel
import com.neupanesushant.wallpaper.view.viewmodels.MainViewModel
import com.neupanesushant.wallpaper.view.viewmodels.ResponseCacheViewModel
import com.neupanesushant.wallpaper.view.viewmodels.SearchedImageViewModel
import com.neupanesushant.wallpaper.view.viewmodels.WallpaperViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
fun appModules() = module {

    val BASE_URL_PEXELS = "https://api.pexels.com/"
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

    single<LocalDataSource>{
        LocalRepositoryImpl(get(), get())
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
        ResponseCacheViewModel(androidApplication(), get(), get())
    }

}